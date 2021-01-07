package com.sehatq.test.model.core

/**
 * The resource object for view models.
 */
class Resource<T> private constructor(var status: Int, var data: T? = null, var error: AppError = AppError(0, null)) {

    companion object {

        const val LOADING = 0
        const val SUCCESS = 1
        const val ERROR = 2

        /** Creates a new loading resource object  */
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(status = LOADING, data = data)
        }

        /**
         * Creates a new successful resource object.
         * @param data the data to be set
         */
        fun <T> success(data: T? = null): Resource<T> {
            return Resource(status = SUCCESS, data = data)
        }

        /**
         * Creates a new error resource object.
         * @param error the error
         */
        fun <T> error(error: AppError, data: T? = null): Resource<T> {
            return Resource(status = ERROR, data = data, error = error)
        }
    }
}
