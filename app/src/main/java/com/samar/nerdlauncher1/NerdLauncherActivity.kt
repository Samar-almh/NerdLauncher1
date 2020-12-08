package com.samar.nerdlauncher1

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "NerdLauncherActivity"

class NerdLauncherActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_launcher)
        recyclerView = findViewById(R.id.app_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        setupAdapter()
    }

    private fun setupAdapter() {
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val activities = packageManager.queryIntentActivities(startupIntent, 0)
        activities.sortWith(Comparator {
                a, b -> String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString()
            )
        })
        recyclerView.adapter = ActivityAdapter(activities)


    }

    private class ActivityHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
         private val nName = itemView.findViewById(R.id.NameText) as TextView
         private val iIcon = itemView.findViewById(R.id.IconButton) as ImageButton
        private lateinit var resolveInfo: ResolveInfo

        init {
            itemView.setOnClickListener(this)
             }

        fun bindActivity(resolveInfo: ResolveInfo) {
            this.resolveInfo = resolveInfo
            val packageManager = itemView.context.packageManager
             val icon = resolveInfo.loadIcon(packageManager)
             val name = resolveInfo.loadLabel(packageManager).toString()
            nName.text=name
             iIcon.setImageDrawable(icon)
        }


        override fun onClick(v: View?) {
            val activityInfo = resolveInfo.activityInfo
            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(
                    activityInfo.applicationInfo.packageName,
                    activityInfo.name
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val context = itemView.context
            context . startActivity (intent)
        }
    }

    private class ActivityAdapter(val activities: List<ResolveInfo>) :
        RecyclerView.Adapter<ActivityHolder>()
    {
        override fun onCreateViewHolder(
            container: ViewGroup,
            viewType: Int):
                ActivityHolder {
            val view=LayoutInflater.from(container.context).inflate(R.layout.icon_nerd_launcher,container,false)
            return ActivityHolder(view)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = activities[position]
            holder.bindActivity(resolveInfo)
        }

        override fun getItemCount(): Int {
            return activities.size
        }
    }
}