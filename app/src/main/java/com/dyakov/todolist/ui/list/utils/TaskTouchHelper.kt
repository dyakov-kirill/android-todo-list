package com.dyakov.todolist.ui.list.utils

import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dyakov.todolist.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class TaskTouchHelper(private val adapter: RecyclerViewAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder.itemViewType == RecyclerViewAdapter.VIEW_TYPE_TASK) {
            val swipeFlags = ItemTouchHelper.END + ItemTouchHelper.START
            makeMovementFlags(0, swipeFlags)
        } else {
            makeMovementFlags(0, 0)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.END -> {
                adapter.onItemChecked((viewHolder as RecyclerViewAdapter.TaskHolder).holdingItem)
               // adapter.notifyItemChanged(viewHolder.layoutPosition)
            }
            ItemTouchHelper.START -> {
                adapter.onItemDismiss((viewHolder as RecyclerViewAdapter.TaskHolder).holdingItem)
            }
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator
            .Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context,
                R.color.red
            ))
            .addSwipeRightBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context,
                R.color.green
            ))
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .addSwipeRightActionIcon(R.drawable.ic_check)
            .setActionIconTint((ContextCompat.getColor(viewHolder.itemView.context, R.color.white)))
            .create().decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}