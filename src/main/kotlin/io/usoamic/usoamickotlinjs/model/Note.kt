package io.usoamic.usoamickotlinjs.model

import io.usoamic.usoamickotlinjs.enumcls.NoteVisibility
import io.usoamic.web3kt.bignumber.BigNumber

data class Note constructor(
    val isExist: Boolean,
    val noteId: BigNumber,
    val visibility: NoteVisibility,
    val noteRefId: BigNumber,
    val content: String,
    val author: String,
    val timestamp: BigNumber
) {
    class Builder {
        private var isExist: Boolean = false
        private lateinit var noteId: BigNumber
        private lateinit var visibility: NoteVisibility
        private lateinit var noteRefId: BigNumber
        private lateinit var content: String
        private lateinit var author: String
        private lateinit var timestamp: BigNumber

        fun setIsExist(isExist: Boolean) = apply {
            this.isExist = isExist
        }

        fun setNoteId(noteId: BigNumber) = apply {
            this.noteId = noteId
        }

        fun setVisibility(visibility: NoteVisibility) = apply {
            this.visibility = visibility
        }

        fun setNoteRefId(refId: BigNumber) = apply {
            this.noteRefId = refId
        }

        fun setContent(content: String) = apply {
            this.content = content
        }

        fun setAuthor(author: String) = apply {
            this.author = author
        }

        fun setTimestamp(timestamp: BigNumber) = apply {
            this.timestamp = timestamp
        }

        fun build() = Note(
            isExist,
            noteId,
            visibility,
            noteRefId,
            content,
            author,
            timestamp
        )
    }
}