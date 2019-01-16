package io.github.luteoos.gent.view.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import de.blox.graphview.BaseGraphAdapter
import de.blox.graphview.Graph
import de.blox.graphview.Node
import io.github.luteoos.gent.R
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.viewmodels.TreeViewModel
import kotlinx.android.synthetic.main.activity_tree.*
import kotlinx.android.synthetic.main.node_tree_person.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import de.blox.graphview.tree.BuchheimWalkerAlgorithm
import de.blox.graphview.tree.BuchheimWalkerConfiguration



class TreeActivity : BaseActivityMVVM<TreeViewModel>() {
    lateinit var treeUUID : String

    override fun getLayoutID(): Int = R.layout.activity_tree

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = TreeViewModel()
        connectToVMMessage()
        getDataFromIntent()
        setBindings()
    }

    override fun onVMMessage(msg: String?) {

    }

    private fun setBindings(){
        ivBack.onClick {
            onBackPressed()
        }
    }

    private fun getDataFromIntent(){
        treeUUID = intent.getStringExtra(Parameters.TREE_UUD)
        tvTreeName.text = intent.getStringExtra(Parameters.TREE_NAME)
        setGraph()
    }

    private fun setGraph(){
        //TODO TEST
        val graphO =  Graph()
        val node1 = Node("texTEst1")
        val node2 = Node("texTEst2")
        val node3 = Node("texTEst3")
        val node4 = Node("texTEst4")
        val node5 = Node("dziecie1")
        val node6 = Node("dziecie6")

        graphO.setAsTree(true)
        graphO.addEdge(node1, node2)
        graphO.addEdge(node1, node3)
        graphO.addEdge(node1, node4)
        graphO.addEdge(node4, node5)
        graphO.addEdge(node4, node6)

        // graphO.addEdge(node4,node3)

        val adapter = object: BaseGraphAdapter<ViewHolder>(this, R.layout.node_tree_person, graphO){
            override fun onCreateViewHolder(view: View?): ViewHolder {
                return ViewHolder(view!!)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder?, data: Any?, position: Int) {
               viewHolder?.text?.text = data.toString()
            }

        }

        graph.adapter = adapter

//        val configuration = BuchheimWalkerConfiguration.Builder()
//            .setSiblingSeparation(100)
//            .setLevelSeparation(300)
//            .setSubtreeSeparation(300)
//            .setOrientation(BuchheimWalkerConfiguration.DEFAULT_SIBLING_SEPARATION)
//            .build()
//        adapter.setAlgorithm(BuchheimWalkerAlgorithm(configuration))
        // you can set the graph via the constructor or use the adapter.setGraph(Graph) method
//        val adapter = BaseGraphAdapter<RecyclerView.ViewHolder>(this,R.layout.node_tree_person, graph){
//            override fun onCreateViewHolder(view: View) : RecyclerView.ViewHolder {
//                return RecyclerView.ViewHolder(view)
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Object data, int position) {
//                viewHolder.mTextView.setText(data.toString());
//            }
//        };
//        graphView.setAdapter(adapter);
//
//        // set the algorithm here
//        final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
//            .setSiblingSeparation(100)
//            .setLevelSeparation(300)
//            .setSubtreeSeparation(300)
//            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
//            .build();
//        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));
    }
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val text = view.textView
    }
}