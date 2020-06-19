package com.stocksexchange.android.model

data class RemoteServiceUrlsWrapper(
    val defaultUrls: RemoteServiceUrls,
    private val _lastWorkingUrls: RemoteServiceUrls?,
    private val _candidateUrls: List<RemoteServiceUrls>
) {


    val hasLastWorkingUrls: Boolean
        get() = (_lastWorkingUrls != null)


    val lastWorkingUrls: RemoteServiceUrls
        get() = (_lastWorkingUrls ?: RemoteServiceUrls.STUB)


    val candidateUrls: List<RemoteServiceUrls>
        get() = mutableListOf<RemoteServiceUrls>().apply {
            if(hasLastWorkingUrls) {
                add(lastWorkingUrls)
            }

            add(defaultUrls)
            addAll(_candidateUrls)
        }


}