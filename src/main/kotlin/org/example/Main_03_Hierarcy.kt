import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
fun main() {
    with(GlobalScope) {
        val parentJob = launch {
            delay(200)
            println("I’m the parent")
            delay(200)
        }
        launch(context = parentJob) {
            delay(200)
            println("I’m a child")
            delay(200)
        }
        if (parentJob.children.iterator().hasNext()) {
            println("The Job has children ${parentJob.children}")
        } else {
            println("The Job has NO children")
        }
        Thread.sleep(1000)
    }
}
