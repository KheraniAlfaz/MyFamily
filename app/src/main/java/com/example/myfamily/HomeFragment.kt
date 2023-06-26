package com.example.myfamily

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Alfaz Kherani",
                "B/901, President Tower, Nr. Tulip Hospital,Ram Chowk,Surat-395007",
                "90%",
                "220"
            ),
            MemberModel(
                "Aliyan Kherani",
                "B/901, President Tower, Nr. Tulip Hospital,Ram Chowk,Surat-395007",
                "80%",
                "200"
            ),
            MemberModel(
                "Rupa Kherani",
                "B/901, President Tower, Nr. Tulip Hospital,Ram Chowk,Surat-395007",
                "70%",
                "357"
            )
            ,MemberModel(
                "Asif Kherani",
                "B/901, President Tower, Nr. Tulip Hospital,Ram Chowk,Surat-395007",
                "60%",
                "800"
            )

        )
        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recyler_view_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}