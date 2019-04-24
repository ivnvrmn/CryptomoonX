package com.rmnivnv.cryptomoonx.top

import com.rmnivnv.cryptomoonx.base.BasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopPresenter(
    override var view: TopContract.View,
    private val repository: TopContract.Repository
) : BasePresenter(), TopContract.Presenter {

    override lateinit var job: Job

    override fun onViewCreated() {
        job = Job()
        loadTopCoins()
    }

    private fun loadTopCoins() {
        launch {
            val topCoins = withContext(Dispatchers.IO) { repository.getTopCoins() }
            topCoins?.also { view.showCoins(it) }
        }
    }

    override fun onDestroy() {
        job.cancel()
    }
}