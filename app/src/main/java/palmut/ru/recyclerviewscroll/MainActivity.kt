package palmut.ru.recyclerviewscroll

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val padding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                resources.displayMetrics).toInt()
        verticalList.addItemDecoration(PaddingDecoration(padding))
        verticalList.adapter = VerticalItemsAdapter().apply { submitList(sampleList) }
    }

    data class Item(val id: Int, val color: Int)

    class DiffUtilCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }

    class VerticalItemViewHolder(item: FixScrollRecyclerView) : RecyclerView.ViewHolder(item)
    class HorizontalItemViewHolder(item: View) : RecyclerView.ViewHolder(item)

    class VerticalItemsAdapter : ListAdapter<Item, VerticalItemViewHolder>(DiffUtilCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalItemViewHolder {
            val padding = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8f,
                    parent.resources.displayMetrics).toInt()
            return VerticalItemViewHolder(
                    FixScrollRecyclerView(parent.context)
                            .apply {
                                addItemDecoration(PaddingDecoration(padding))
                                layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
                                adapter = HorizontalItemsAdapter().apply { submitList(sampleList) }
                            }
            )
        }

        override fun onBindViewHolder(holder: VerticalItemViewHolder, position: Int) {
        }
    }

    class HorizontalItemsAdapter
        : ListAdapter<Item, HorizontalItemViewHolder>(DiffUtilCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalItemViewHolder {
            val size = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    120f,
                    parent.resources.displayMetrics).toInt()

            return HorizontalItemViewHolder(View(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(size, size)
            })
        }

        override fun onBindViewHolder(holder: HorizontalItemViewHolder, position: Int) {
            val item = getItem(position)
            holder.itemView.background = ColorDrawable(item.color)
        }
    }

    class PaddingDecoration(val padding: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(padding, padding, padding, padding)
        }
    }

    companion object {
        val sampleList = listOf(
                Item(0, Color.BLUE),
                Item(1, Color.RED),
                Item(2, Color.GRAY),
                Item(3, Color.GREEN),
                Item(4, Color.DKGRAY),
                Item(5, Color.MAGENTA),
                Item(6, Color.CYAN),
                Item(7, Color.LTGRAY),
                Item(8, Color.YELLOW),
                Item(9, Color.WHITE),
                Item(10, Color.BLACK)
        )
    }
}
