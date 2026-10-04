package com.example.disasterpreparednessapp.feature_map.data.repository

import com.example.disasterpreparednessapp.feature_map.data.remote.HelpRequestDto
import com.example.disasterpreparednessapp.feature_map.data.remote.toDomain
import com.example.disasterpreparednessapp.feature_map.data.remote.toDto
import com.example.disasterpreparednessapp.feature_map.domain.model.HelpRequest
import com.example.disasterpreparednessapp.feature_map.domain.repository.HelpRequestRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HelpRequestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HelpRequestRepository {

    private val collectionRef = firestore.collection("help_requests")

    override fun observeActiveRequests(ttlMillis: Long): Flow<List<HelpRequest>> = callbackFlow {
        val minTimestamp = System.currentTimeMillis() - ttlMillis
        val listenerRegistration = collectionRef
            .whereEqualTo("status", "active")
            .whereGreaterThanOrEqualTo("timestamp", minTimestamp)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val now = System.currentTimeMillis()
                val requests = snapshot?.documents.orEmpty().mapNotNull { doc ->
                    doc.toObject(HelpRequestDto::class.java)?.copy(id = doc.id)?.toDomain()
                }.filter { req ->
                    (now - req.timestamp) <= ttlMillis
                }
                trySend(requests)
            }

        awaitClose { listenerRegistration.remove() }
    }

    override fun observeUserActiveRequest(userId: String): Flow<HelpRequest?> = callbackFlow {
        if (userId.isBlank()) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listenerRegistration = collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "active")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                val activeDoc = snapshot?.documents?.firstOrNull()
                val request = activeDoc?.toObject(HelpRequestDto::class.java)?.copy(id = activeDoc.id)?.toDomain()
                trySend(request)
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun sendHelpRequest(request: HelpRequest): Result<Unit> = runCatching {
        val docRef = if (request.id.isNotBlank()) {
            collectionRef.document(request.id)
        } else if (request.userId.isNotBlank()) {
            collectionRef.document(request.userId)
        } else {
            collectionRef.document()
        }
        val finalRequest = request.copy(id = docRef.id)
        docRef.set(finalRequest.toDto()).await()
    }

    override suspend fun removeHelpRequest(userId: String): Result<Unit> = runCatching {
        if (userId.isBlank()) return@runCatching
        val activeSnapshot = collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "active")
            .get()
            .await()

        for (doc in activeSnapshot.documents) {
            doc.reference.update("status", "resolved").await()
        }
    }
}
