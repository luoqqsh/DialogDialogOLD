package x.com.hl_base.activity

import kotlinx.coroutines.*
import x.com.hl_base.interfaces.OnSimpleListener
import x.com.hl_base.interfaces.OnTSimpleListener

abstract class KBaseActivity : HLSlideActivity(), CoroutineScope by MainScope() {
    fun KTaskManager(hOnListener1: OnTSimpleListener<Any>) {
        KTaskManager(0, hOnListener1)
    }

    fun KTaskManager(delay: Long, hOnListener1: OnTSimpleListener<Any>) {
        launch {
            val deferred = async(Dispatchers.IO) {
                delay(delay)
                hOnListener1.onBackGround()
            }
            hOnListener1.onUI(deferred.await())
        }
    }

    fun KTaskManagerB(hOnListener1: OnSimpleListener) {
        KTaskManagerB(0, hOnListener1)
    }

    fun KTaskManagerB(delay: Long, hOnListener1: OnSimpleListener) {
        launch {
            async(Dispatchers.IO) {
                delay(delay)
                hOnListener1.onListen()
            }
        }
    }

    fun cancelTask(){
        cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}