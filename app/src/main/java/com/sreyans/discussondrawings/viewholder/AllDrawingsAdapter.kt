import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.databinding.ItemDrawingViewholderBinding
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.viewholder.ItemDrawingViewHolder

class AllDrawingsAdapter(context: Context, drawings: ArrayList<Drawing>) : RecyclerView.Adapter<ItemDrawingViewHolder>() {
    private val context: Context
    private val drawings : ArrayList<Drawing>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDrawingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemDrawingViewholderBinding: ItemDrawingViewholderBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_drawing_viewholder, parent, false)
        return ItemDrawingViewHolder(itemDrawingViewholderBinding)
    }

    override fun onBindViewHolder(holder: ItemDrawingViewHolder, position: Int) {
        (holder as ItemDrawingViewHolder).onBind(context, drawings[position])
    }

    override fun getItemCount(): Int {
        return this.drawings.size
    }

    fun setList(list: ArrayList<Drawing>) {
        this.drawings.clear()
        this.drawings.addAll(list)
    }

    init {
        this.drawings = drawings
        this.context = context
    }
}