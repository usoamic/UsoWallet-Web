package io.usoamic.usoamickotlinjs.model

import io.usoamic.usoamickotlinjs.enumcls.IdeaStatus
import io.usoamic.web3kt.bignumber.BigNumber

data class Idea constructor(
    val isExist: Boolean,
    val ideaId: BigNumber,
    val ideaRefId: BigNumber,
    val author: String,
    val description: String,
    val ideaStatus: IdeaStatus,
    val timestamp: BigNumber,
    val numberOfSupporters: BigNumber,
    val numberOfAbstained: BigNumber,
    val numberOfVotedAgainst: BigNumber,
    val numberOfParticipants: BigNumber
) {
    class Builder {
        private var isExist: Boolean = false
        private lateinit var ideaId: BigNumber
        private lateinit var ideaRefId: BigNumber
        private lateinit var author: String
        private lateinit var description: String
        private lateinit var ideaStatus: IdeaStatus
        private lateinit var timestamp: BigNumber
        private lateinit var numberOfSupporters: BigNumber
        private lateinit var numberOfAbstained: BigNumber
        private lateinit var numberOfVotedAgainst: BigNumber
        private lateinit var numberOfParticipants: BigNumber

        fun setIsExist(isExist: Boolean) = apply {
            this.isExist = isExist
        }

        fun setIdeaId(ideaId: BigNumber) = apply {
            this.ideaId = ideaId
        }

        fun setIdeaRefId(ideaRefId: BigNumber) = apply {
            this.ideaRefId = ideaRefId
        }

        fun setAuthor(author: String) = apply {
            this.author = author
        }

        fun setDescription(description: String) = apply {
            this.description = description
        }

        fun setIdeaStatus(ideaStatus: IdeaStatus) = apply {
            this.ideaStatus = ideaStatus
        }

        fun setTimestamp(timestamp: BigNumber) = apply {
            this.timestamp = timestamp
        }

        fun setNumberOfSupporters(numberOfSupporters: BigNumber) = apply {
            this.numberOfSupporters = numberOfSupporters
        }

        fun setNumberOfAbstained(numberOfAbstained: BigNumber) = apply {
            this.numberOfAbstained = numberOfAbstained
        }

        fun setNumberOfVotedAgainst(numberOfVotedAgainst: BigNumber) = apply {
            this.numberOfVotedAgainst = numberOfVotedAgainst
        }

        fun setNumberOfParticipants(numberOfParticipants: BigNumber) = apply {
            this.numberOfParticipants = numberOfParticipants
        }

        fun build() = Idea(
            isExist,
            ideaId,
            ideaRefId,
            author,
            description,
            ideaStatus,
            timestamp,
            numberOfSupporters,
            numberOfAbstained,
            numberOfVotedAgainst,
            numberOfParticipants
        )
    }
}