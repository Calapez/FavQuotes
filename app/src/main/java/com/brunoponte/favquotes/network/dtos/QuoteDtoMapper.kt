package com.brunoponte.favquotes.network.dtos

import com.brunoponte.favquotes.domainModels.Quote

class QuoteDtoMapper {

    companion object {

        fun mapToDomainModel(dto: QuoteDto) = Quote (
            id = dto.id,
            isDialogue = dto.isDialogue,
            isPrivate = dto.isPrivate,
            tags = dto.tags,
            url = dto.url,
            favorites = dto.favorites,
            upvotes = dto.upvotes,
            downvotes = dto.downvotes,
            author = dto.author,
            authorLink = dto.authorLink,
            body = dto.body,
        )

        fun toDomainModelList(dtoList: List<QuoteDto>) = dtoList.map { dto ->
            mapToDomainModel(dto)
        }

    }

}

