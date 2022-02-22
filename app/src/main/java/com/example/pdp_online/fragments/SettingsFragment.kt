package com.example.pdp_online.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.pdp_online.BuildConfig
import com.example.pdp_online.R
import com.example.pdp_online.adapter.KursAdapter
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.DeleteDialogBinding
import com.example.pdp_online.databinding.FragmentSettingsBinding
import com.example.pdp_online.entity.Kurs

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSettingsBinding
    lateinit var appDatabase: AppDatabase

    lateinit var photoURI: Uri
    lateinit var currentImagePath: String
    private var fileAbsolutePath: String? = null

    private lateinit var kursAdapter: KursAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        binding.rasm.setOnClickListener {
            setPhoto()
        }


        kursAdapter = KursAdapter(object:KursAdapter.OnItemClickListener{
            override fun onItemClick(kurs: Kurs) {
                var bundle = Bundle()
                bundle.putSerializable("shit",kurs)
                findNavController().navigate(R.id.add_ModuleFragment,bundle)
            }

            override fun onItemDeleteClick(kurs: Kurs, position: Int) {
                val alertDialog = AlertDialog.Builder(binding.root.context)
                val dialog = alertDialog.create()
                val dialogView = DeleteDialogBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    null,
                    false
                )

                dialogView.ha.setOnClickListener {
                    appDatabase.kursDao().deleteKurs(kurs)
                    dialog.dismiss()
                }

                dialogView.yoq.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setView(dialogView.root)
                dialog.show()
            }

            override fun onItemEditClick(kurs: Kurs, position: Int) {
                var bundle = Bundle()
                bundle.putSerializable("key",kurs)
                findNavController().navigate(R.id.kurs_EditFragment,bundle)
            }


        })



        appDatabase.kursDao().getAllKurs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Consumer<List<Kurs>> {
                override fun accept(t: List<Kurs>?) {
                    kursAdapter.submitList(t)
                }

            }, object : Consumer<Throwable>{
                override fun accept(t: Throwable?) {

                }

            })
        binding.rv.adapter = kursAdapter






        return binding.root
    }

    private fun setPhoto() {
        val picturesDialog = AlertDialog.Builder(binding.root.context)
        val dialog = picturesDialog.create()
        picturesDialog.setNegativeButton("Bekor qilish",{ dialogInterFace: DialogInterface, i: Int ->
            dialog.dismiss()
        })
        picturesDialog.setTitle("Kamera yoki Gallerreyani tanlang")
        val DialogItems = arrayOf("Galereyadan rasm tanlash", "Kamera orqali rasmga olish")
        picturesDialog.setItems(DialogItems){
                dialog, which ->
            when(which){
                0 -> permission_gallery()
                1 -> permission_camera()
            }
        }
        picturesDialog.show()
    }

    private fun permission_camera() {
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    /* ... */
                    Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
                    val imageFile = createImageFile()
                    photoURI = FileProvider.getUriForFile(binding.root.context,
                        BuildConfig.APPLICATION_ID,imageFile)
                    getTakeImageContent.launch(photoURI)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                    Toast.makeText(binding.root.context, "Denied", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("Permission")
                        .setMessage("Oka qabul qivoraqoling iltimos")
                        .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.cancelPermissionRequest()
                        })
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        })
                        .show()
                }
            }).check()
    }
    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){

            if (it) {
                binding.rasm.setImageURI(photoURI)
                val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
                val filesDir = binding.root.context.filesDir
                val contentResolver = activity?.contentResolver
                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir,"image.jpg$format")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                fileAbsolutePath = file.absolutePath


                binding.floatingActionButton.setOnClickListener {
                    val kurs = Kurs()
                    kurs.kr_image = fileAbsolutePath
                    kurs.kr_name = binding.kursName.text.toString()
                    appDatabase.kursDao().addKurs(kurs)
                    binding.kursName.setText("")
                }




            }
        }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format",".jpg",externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    private fun permission_gallery() {
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    /* ... */
                    Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
                    pickImageFromGallery()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                    Toast.makeText(binding.root.context, "Denied", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("Permission")
                        .setMessage("Oka qabul qivoraqoling iltimos")
                        .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.cancelPermissionRequest()
                        })
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        })
                        .show()
                }
            }).check()
    }

    private fun pickImageFromGallery() {
        getImageContent.launch("image/*")
    }
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.rasm.setImageURI(uri)
            val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())

            val filesDir = binding.root.context.filesDir
            val contentResolver = activity?.contentResolver
            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jp$format")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()




            val AbsolutePath = file.absolutePath
            val fileInputStream = FileInputStream(file)

            binding.floatingActionButton.setOnClickListener {
                val kurs = Kurs()
                kurs.kr_image = AbsolutePath
                kurs.kr_name = binding.kursName.text.toString()
                Observable.fromCallable {
                    appDatabase.kursDao().addKurs(kurs)
                }.subscribe{
                    Toast.makeText(binding.root.context, "added", Toast.LENGTH_SHORT).show()
                    binding.kursName.setText("")

                }

            }

//            kursAdapter = KursAdapter()
//
//
//
//            appDatabase.kursDao().getAllKurs()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object: Consumer<List<Kurs>> {
//                    override fun accept(t: List<Kurs>?) {
//                        kursAdapter.submitList(t)
//                    }
//
//                }, object : Consumer<Throwable>{
//                    override fun accept(t: Throwable?) {
//
//                    }
//
//                })
//            binding.rv.adapter = kursAdapter






        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}