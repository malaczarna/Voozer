package pl.voozer.service.bubble

import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bsk.floatingbubblelib.FloatingBubbleConfig
import com.bsk.floatingbubblelib.FloatingBubbleService
import pl.voozer.R



class FloatingService: FloatingBubbleService() {
    override fun getConfig(): FloatingBubbleConfig {
        return FloatingBubbleConfig.Builder()
            .bubbleIcon(ContextCompat.getDrawable(context, R.drawable.bubble))
            .bubbleIconDp(92)
            .paddingDp(4)
            .borderRadiusDp(4)
            .physicsEnabled(true)
            .expandableColor(Color.WHITE)
            .triangleColor(Color.WHITE)
            .gravity(Gravity.END)
            .removeBubbleAlpha(0.75f)
            .expandableView(expandableView)
            .build()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val message = intent.getStringExtra("message")
        val view = getInflater().inflate(R.layout.floating_notification, null)
        (view.findViewById(R.id.tvTitle) as TextView).text = message
        expandableView = view
        return super.onStartCommand(intent, flags, startId)
    }
}