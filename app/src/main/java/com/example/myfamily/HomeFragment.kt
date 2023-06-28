package com.example.myfamily

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val listContacts : ArrayList<ContactModel> = ArrayList()
    lateinit var  inviteAdapter : InviteAdapter


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

        Log.d("FetchContacts", "onViewCreated: ")

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


        Log.d("FetchContacts", "fetchContacts: is going to start")

        Log.d("FetchContacts", "fetchContacts: is now started. listcontacts = ${listContacts.size}")

        inviteAdapter = InviteAdapter(listContacts)
        fetchDatabaseContacts()

        Log.d("FetchContacts", "fetchContacts: has now ended")

        CoroutineScope(Dispatchers.IO).launch {

            Log.d("FetchContacts", "fetchContacts: coroutine start")

            insertDatabaseContacts(fetchContacts())

            Log.d("FetchContacts", "fetchContacts: coroutine end. listcontacts = ${listContacts.size}")

        }




        val inviterecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviterecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviterecycler.adapter = inviteAdapter


        val threeDots = requireView().findViewById<ImageView>(R.id.icon_three_dots)
        threeDots.setOnClickListener {

            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Signed Out", Toast.LENGTH_LONG).show()
            val i = Intent(activity, SplashScreen::class.java)
            startActivity(i)
        }
    }

    private fun fetchDatabaseContacts(): Void? {
        val database = MyFamilyDatabase.getDatabase(requireContext())

        database.contactDAO().getAllContacts().observe(viewLifecycleOwner){

            Log.d("FetchContacts", "fetchDatabaseContacts: ")

            listContacts.clear()
            listContacts.addAll(it)

            inviteAdapter.notifyDataSetChanged()

        }
        return null
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {

        val database = MyFamilyDatabase.getDatabase(requireContext())

        database.contactDAO().insertAll(listContacts)
    }

    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<ContactModel> {

        Log.d("FetchContacts", "fetchContacts: started")

        val cr = requireActivity().contentResolver
        val cursor =  cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)

        val listContacts: ArrayList<ContactModel> = ArrayList()

        if(cursor != null && cursor.count >0){

            while(cursor != null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber > 0){

                    val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
                            arrayOf(id),
                            ""
                        )
                    if(pCur!=null && pCur.count > 0){

                        while (pCur!=null && pCur.moveToNext()){

                            val phoneNum = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(ContactModel(name,phoneNum))

                        }
                        pCur.close()

                    }


                }

            }
            if(cursor!=null){
                cursor.close()
            }
        }

        Log.d("FetchContacts", "fetchContacts: ended")

        return listContacts
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}