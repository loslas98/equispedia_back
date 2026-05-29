package com.example.equispedia.DTO

data class BedTypeRequest(
    val name: String
)

data class BedTypeResponse(
    val id: Int,
    val name: String
)

data class RoomBedRequest(
    val bedTypeId: Int,
    val count: Int
)

data class RoomBedResponse(
    val bedType: BedTypeResponse,
    val count: Int
)
