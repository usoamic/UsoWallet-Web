package io.usoamic.usoamickotlinjs.core

import io.usoamic.usoamickotlinjs.enumcls.IdeaStatus
import io.usoamic.usoamickotlinjs.model.Idea
import io.usoamic.usoamickotlinjs.model.Vote
import io.usoamic.web3kt.core.contract.response.CallResponse
import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Ideas : Owner {
    @JsName("addIdea")
    fun addIdea(description: String): ExecuteResponse

    @JsName("setIdeaStatus")
    fun setIdeaStatus(ideaRefId: String, status: IdeaStatus): ExecuteResponse

    @JsName("supportIdea")
    fun supportIdea(ideaRefId: String, comment: String): ExecuteResponse

    @JsName("abstainIdea")
    fun abstainIdea(ideaRefId: String, comment: String): ExecuteResponse

    @JsName("againstIdea")
    fun againstIdea(ideaRefId: String, comment: String): ExecuteResponse

    @JsName("getIdea")
    fun getIdea(ideaRefId: String): CallResponse<Idea>

    @JsName("getIdeaByAuthor")
    fun getIdeaByAuthor(author: String, ideaId: String): CallResponse<Idea>

    @JsName("getVote")
    fun getVote(ideaRefId: String, voteRefId: String): CallResponse<Vote>

    @JsName("getVoteByVoter")
    fun getVoteByVoter(voter: String, voteId: String): CallResponse<Vote>

    @JsName("getNumberOfIdeas")
    fun getNumberOfIdeas(): CallResponse<String>

    @JsName("getNumberOfIdeasByAuthor")
    fun getNumberOfIdeasByAuthor(author: String): CallResponse<String>

    @JsName("getNumberOfVotesByVoter")
    fun getNumberOfVotesByVoter(voter: String): CallResponse<String>

    @JsName("getLastIdeaId")
    fun getLastIdeaId(): CallResponse<String>

    @JsName("getLastIdeaIdByAuthor")
    fun getLastIdeaIdByAuthor(author: String): CallResponse<String>

}