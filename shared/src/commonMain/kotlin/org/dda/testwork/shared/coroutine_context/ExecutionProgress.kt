package org.dda.testwork.shared.coroutine_context


interface ExecutionProgressParam

sealed class ExecutionProgress(
    open val isModal: Boolean = false,
    open val progressInt: Int = 0,
    open val progressFloat: Float = 0F,
    open val param: ExecutionProgressParam?
) {

    open class Global(
        isModal: Boolean = false,
        progressInt: Int = 0,
        progressFloat: Float = 0F,
        param: ExecutionProgressParam? = null
    ) : ExecutionProgress(isModal, progressInt, progressFloat, param) {

        fun copy(
            isModal: Boolean = this.isModal,
            progressInt: Int = this.progressInt,
            progressFloat: Float = this.progressFloat,
            param: ExecutionProgressParam? = this.param
        ): Global = Global(isModal, progressInt, progressFloat, param)
    }

    open class Local(
        isModal: Boolean = false,
        progressInt: Int = 0,
        progressFloat: Float = 0F,
        param: ExecutionProgressParam? = null
    ) : ExecutionProgress(isModal, progressInt, progressFloat, param) {

        fun toGlobal() = Global(
            isModal = isModal,
            progressInt = progressInt,
            progressFloat = progressFloat,
            param = param
        )

    }

    open fun beforeShow() {}

    open fun beforeHide() {}
}

val executionProgressGlobalDefault = ExecutionProgress.Global()
val executionProgressLocalDefault = ExecutionProgress.Local()