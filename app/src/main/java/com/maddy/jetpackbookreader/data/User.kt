package com.maddy.jetpackbookreader.data

data class User(
    val id: Any,
    val email: String,
    val fullName: String,
    val verificationStatus: VerificationStatus,
    val memberShipStatus: MemberShipStatus
) {
    enum class VerificationStatus {
        Verified,
        NotVerified;
    }
    enum class MemberShipStatus {
        Free,
        Paid;
    }
}