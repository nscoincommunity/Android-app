package com.stocksexchange.android.ui.base.dataloading

import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel.BaseDataLoadingActionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A base model of the MVP architecture that loads simple
 * data, meaning data that does not require any additions and
 * transformations.
 */
abstract class BaseDataLoadingSimpleModel<
    Data,
    in Parameters,
    ActionListener: BaseDataLoadingActionListener<Data>
> : BaseDataLoadingModel<Data, Parameters, ActionListener>() {


    override suspend fun performDataLoading(params: Parameters) {
        val repositoryResult = getRepositoryResult(params)

        withContext(Dispatchers.Main) {
            repositoryResult
                .onSuccess { handleSuccessfulResponse(it) }
                .onFailure { handleUnsuccessfulResponse(it) }
        }
    }


    /**
     * A method directly responsible for fetching data.
     *
     * @param params The parameters for loading data
     *
     * @return The repository result
     */
    protected abstract suspend fun getRepositoryResult(params: Parameters): RepositoryResult<Data>


}