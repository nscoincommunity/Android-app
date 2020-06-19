package com.stocksexchange.android.ui.base.mvp.presenters

import androidx.annotation.CallSuper
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.socket.SocketConnection
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.base.mvp.model.Model
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.core.utils.listeners.OnBackPressListener
import com.stocksexchange.core.utils.listeners.OnNavigateUpListener
import org.greenrobot.eventbus.EventBus
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A base presenter of the MVP architecture.
 */
abstract class BasePresenter<out V, out M>(
    protected val mView: V,
    protected val mModel: M
) : Presenter, BaseModel.BaseActionListener, KoinComponent,
    CanObserveNetworkStateChanges, OnNavigateUpListener,
    OnBackPressListener where
        V : BaseView,
        M : Model {


    protected val mStringProvider: StringProvider by inject()

    protected val mSocketConnection: SocketConnection by inject()




    override fun start() {
        mModel.start()

        if(canReceiveEvents()) {
            EventBus.getDefault().register(this)
        }

        startListeningToSocketEvents()
    }


    override fun stop() {
        mModel.stop()

        mView.hideMaterialDialog()

        if(canReceiveEvents()) {
            EventBus.getDefault().unregister(this)
        }
    }


    /**
     * Should be overridden in case there is a need to start listening
     * to socket events.
     */
    protected open fun startListeningToSocketEvents() {
        // Stub
    }

    /**
     * Should be overridden in case there is a need to stop listening
     * to socket events.
     */
    protected open fun stopListeningToSocketEvents() {
        // Stub
    }


    protected fun showInfoDialog(title: String = "", content: String = "") {
        mView.showMaterialDialog(MaterialDialogBuilder.infoDialog(
            title = title,
            content = content,
            stringProvider = mStringProvider
        ))
    }


    protected fun showErrorDialog(content: String) {
        mView.showMaterialDialog(MaterialDialogBuilder.errorDialog(
            content = content,
            stringProvider = mStringProvider
        ))
    }


    override fun onRequestSent(requestType: Int) {
        // Stub
    }


    override fun onResponseReceived(requestType: Int) {
        // Stub
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        // Stub
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        // Stub
    }


    override fun onViewSelected() {
        // Left for subclass implementations
    }


    override fun onViewUnselected() {
        // Left for subclass implementations
    }


    override fun onNetworkConnected() {
        // Left for subclass implementations
    }


    override fun onNetworkDisconnected() {
        // Left for subclass implementations
    }


    override fun onNavigateUpPressed(): Boolean {
        return mView.navigateBack()
    }


    override fun onBackPressed(): Boolean {
        return false
    }


    /**
     * Returns a boolean value denoting whether this
     * presenter can receive EventBus events or not.
     *
     * @return true if can; false otherwise
     */
    protected open fun canReceiveEvents(): Boolean = false


    @CallSuper
    override fun onRestoreState(savedState: SavedState) {
        mModel.onRestoreState(savedState)
    }


    @CallSuper
    override fun onSaveState(savedState: SavedState) {
        mModel.onSaveState(savedState)
    }


    @CallSuper
    override fun onRecycle() {
        stopListeningToSocketEvents()
    }


    override fun toString(): String = javaClass.simpleName


}