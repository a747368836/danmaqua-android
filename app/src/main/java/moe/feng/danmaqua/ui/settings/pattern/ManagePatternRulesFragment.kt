package moe.feng.danmaqua.ui.settings.pattern

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.MultiTypeAdapter
import kotlinx.android.synthetic.main.manage_pattern_rules_layout.*
import moe.feng.danmaqua.Danmaqua
import moe.feng.danmaqua.Danmaqua.ACTION_PREFIX
import moe.feng.danmaqua.R
import moe.feng.danmaqua.api.DanmaquaApi
import moe.feng.danmaqua.model.PatternRulesItem
import moe.feng.danmaqua.ui.BaseFragment
import moe.feng.danmaqua.ui.list.BaseViewHolder
import moe.feng.danmaqua.ui.list.SimpleDiffItemCallback

class ManagePatternRulesFragment : BaseFragment(), PatternRulesItemDelegate.Callback {

    companion object {

        const val ACTION = "$ACTION_PREFIX.settings.MANAGE_PATTERN_RULES"

    }

    private val adapter: MultiTypeAdapter = MultiTypeAdapter().also {
        it.register(PatternRulesItemDelegate(this))
        it.register(AddButtonDelegate())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.manage_pattern_rules_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerForContextMenu(recyclerView)

        recyclerView.adapter = adapter

        launchWhenCreated {
            loadList()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {

                return true
            }
            R.id.action_delete -> {

                return true
            }
            R.id.action_info -> {

                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterForContextMenu(recyclerView)
    }

    override fun getActivityTitle(context: Context): String? {
        return context.getString(R.string.manage_pattern_rules_title)
    }

    private suspend fun loadList() {
        val dao = database.patternRules()
        val localRules = dao.getAll().toMutableList()
        var oldItem = adapter.items.toList()
        adapter.items = mutableListOf("add") + localRules
        DiffUtil.calculateDiff(DiffUtilCallback(oldItem, adapter.items))
            .dispatchUpdatesTo(adapter)
        try {
            val onlineRules = DanmaquaApi.getPatternRules().data.filter { onlineRule ->
                localRules.find { it.id == onlineRule.id }?.let {
                    it.title = onlineRule.title
                    it.desc = onlineRule.desc
                    it.pattern = onlineRule.pattern
                    it.committer = onlineRule.committer
                    it.local = false
                    dao.update(it)
                    return@filter false
                }
                return@filter true
            }
            oldItem = adapter.items.toList()
            adapter.items = mutableListOf("add") + localRules + onlineRules
            DiffUtil.calculateDiff(DiffUtilCallback(oldItem, adapter.items))
                .dispatchUpdatesTo(adapter)
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO Show failed to get online rules
        }
    }

    override fun onItemCheck(item: PatternRulesItem) {
        launchWhenStarted {
            val dao = database.patternRules()
            dao.getAll().forEach {
                it.selected = it.id == item.id
                dao.update(it)
            }
            if (!item.local) {
                if (dao.findById(item.id) == null) {
                    item.selected = true
                    dao.add(item)
                }
            }
            Danmaqua.Settings.notifyChanged()
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }
    }

    private fun onAddButtonClick() {

    }

    private inner class AddButtonDelegate
        : ItemViewDelegate<String, AddButtonDelegate.ViewHolder>() {

        inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

            init {
                itemView.setOnClickListener {
                    onAddButtonClick()
                }
            }

        }

        override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
            return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.manage_pattern_rules_add_button, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, item: String) {}

    }

    private class DiffUtilCallback(oldItems: List<Any>, newItems: List<Any>)
        : SimpleDiffItemCallback<Any>(oldItems, newItems) {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is PatternRulesItem && newItem is PatternRulesItem) {
                return oldItem.id == newItem.id
            }
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is PatternRulesItem && newItem is PatternRulesItem) {
                return oldItem == newItem
            }
            return super.areContentsTheSame(oldItem, newItem)
        }

    }

}