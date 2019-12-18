package pl.voozer.service.model

enum class NotificationType(type: String) {
    ASK("ASK"),
    ACCEPT("ACCEPT"),
    DECLINE("DECLINE"),
    MEETING("MEETING"),
    RATING("RATING")
}