package com.prayatna.storyapp.data.remote.response

data class GetStoryResponse(
	val listStory: List<ListStory?>? = null,
	val error: Boolean? = null,
	val message: String? = null
)

data class ListStory(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val lon: Any? = null,
	val id: String? = null,
	val lat: Any? = null
)

