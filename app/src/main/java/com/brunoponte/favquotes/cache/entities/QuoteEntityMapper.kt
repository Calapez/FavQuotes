package com.brunoponte.favquotes.cache.entities

import com.brunoponte.favquotes.domainModels.Quote

class QuoteEntityMapper {

    companion object {

        fun mapToDomainModel(entity: QuoteEntity) = Quote (
            id = entity.id,
            isDialogue = entity.isDialogue,
            isPrivate = entity.isPrivate,
            tags = entity.tags,
            url = entity.url,
            favorites = entity.favorites,
            upvotes = entity.upvotes,
            downvotes = entity.downvotes,
            author = entity.author,
            authorLink = entity.authorLink,
            body = entity.body,
        )

        fun mapToEntity(model: Quote) = QuoteEntity (
            id = model.id ?: -1,
            isDialogue = model.isDialogue,
            isPrivate = model.isPrivate,
            tags = model.tags,
            url = model.url,
            favorites = model.favorites,
            upvotes = model.upvotes,
            downvotes = model.downvotes,
            author = model.author,
            authorLink = model.authorLink,
            body = model.body,
        )

        fun toEntityList(domainModelList: List<Quote>) = domainModelList.map { domainModel ->
            mapToEntity(domainModel)
        }

        fun toDomainModelList(entityList: List<QuoteEntity>) = entityList.map { entity ->
            mapToDomainModel(entity)
        }

    }

}