package be.sourcedbvba.restbucks.domain.notification

data class Notification(val message: String)

data class Notifications(val list: List<Notification> = listOf()) {
    fun hasNotifications() = list.isNotEmpty()
}