package com.rmnivnv.cryptomoonx.top

import androidx.recyclerview.widget.DiffUtil
import com.rmnivnv.cryptomoonx.base.BasePresenter
import com.rmnivnv.cryptomoonx.model.Preferences
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopPresenter(
    override var view: TopContract.View,
    private val repository: TopContract.Repository,
    private val preferences: Preferences,
    private val currentTime: () -> Long
) : BasePresenter(), TopContract.Presenter {

    override lateinit var job: Job
    private var oldCoins: List<TopCoinViewEntity> = listOf()

    override fun onViewCreated() {
        job = Job()
        view.showRefreshAnimation()
        loadTopCoins()
    }

    private fun loadTopCoins() {
        launch {
            val topCoins = withContext(Dispatchers.IO) { repository.getTopCoins() }
            topCoins?.also {
                val diffResult = withContext(Dispatchers.IO) {
                    preferences.topCoinsUpdatedTime = currentTime()
                    DiffUtil.calculateDiff(TopCoinsDiffUtilCallback(oldCoins, it))
                }
                oldCoins = it

                with(view) {
                    showCoins(it, diffResult)
                    updateTime()
                    hideRefreshAnimation()
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun onRefresh() {
        loadTopCoins()
    }
}