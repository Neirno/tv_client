// В вашем core.extension пакете
package com.neirno.tv_client.data.util

import com.neirno.tv_client.data.data_source.entity.YoutubeSearch as DataYoutubeSearch
import com.neirno.tv_client.domain.entity.YoutubeSearch as DomainYoutubeSearch

fun DataYoutubeSearch.toDomain(): DomainYoutubeSearch {
    return DomainYoutubeSearch(id = this.id, query = this.query)
}

fun DomainYoutubeSearch.toData(): DataYoutubeSearch {
    return DataYoutubeSearch(id = this.id, query = this.query)
}
