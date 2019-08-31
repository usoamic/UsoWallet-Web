package io.usoamic.usoamickotlinjs.model

import io.usoamic.usoamickotlinjs.enumcls.VoteType
import io.usoamic.web3kt.bignumber.BigNumber

data class Vote constructor(
    val isExist: Boolean,
    val ideaRefId: BigNumber,
    val voteRefId: BigNumber,
    val voteId: BigNumber,
    val voter: String,
    val voteType: VoteType,
    val comment: String
) {
    class Builder {
        private var isExist: Boolean = false
        private lateinit var ideaRefId: BigNumber
        private lateinit var voteId: BigNumber
        private lateinit var voteRefId: BigNumber
        private lateinit var voter: String
        private lateinit var voteType: VoteType
        private lateinit var comment: String

        fun setIsExist(exist: Boolean) = apply {
            this.isExist = exist
        }

        fun setIdeaRefId(ideaId: BigNumber) = apply {
            this.ideaRefId = ideaId
        }

        fun setVoteId(voteId: BigNumber) = apply {
            this.voteId = voteId
        }

        fun setVoteRefId(voteRefId: BigNumber) = apply {
            this.voteRefId = voteRefId
        }

        fun setVoter(address: String) = apply {
            this.voter = address
        }

        fun setVoteType(voteType: VoteType) = apply {
            this.voteType = voteType
        }

        fun setComment(comment: String) = apply {
            this.comment = comment
        }

        fun build() = Vote(
            isExist,
            ideaRefId,
            if(::voteRefId.isInitialized) voteRefId else BigNumber("-1"),
            voteId,
            voter,
            voteType,
            comment
        )
    }
}