package com.neirno.tv_client.domain.use_case.youtube

data class YoutubeUseCase(
    val searchVideo: SearchVideo,
    val sendVideoUrl: SendVideoUrl,
    val insertYoutubeSearch: InsertYoutubeSearch,
    val getYoutubeSearches: GetYoutubeSearches,
    val getYoutubeSearch: GetYoutubeSearch,
    val deleteYoutubeSearch: DeleteYoutubeSearch,
    val deleteOldYoutubeSearchies: DeleteOldYoutubeSearchies
)
