package x.com.hl_base.hl_fragment

import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import x.com.hl_base.hl_interface.OnSimpleListener
import x.com.hl_base.hl_interface.OnTSimpleListener

abstract class KBaseFragment : Fragment(), CoroutineScope by MainScope() {
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