package com.example.healthcarecomp.ui.chatmessage

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.Attachment
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.databinding.FragmentChatMessageBinding
import com.example.healthcarecomp.helper.FileHelper
import com.example.healthcarecomp.helper.ImageSelectorHelper
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class ChatMessageFragment : BaseFragment(R.layout.fragment_chat_message) {
    private lateinit var _binding: FragmentChatMessageBinding
    private var _parent: MainActivity? = null
    private val args: ChatMessageFragmentArgs by navArgs()
    private lateinit var _recyclerViewAdapter: ChatMessageRecyclerViewAdapter
    private lateinit var _recyclerViewFileSelected: FileSelectedRecyclerViewAdapter
    private lateinit var _viewModel: ChatMessageViewModel
    private val imageSelectorHelper = ImageSelectorHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatMessageBinding.inflate(layoutInflater, container, false)
        _parent = requireActivity() as? MainActivity

        //set up action click back
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPage(R.id.action_chatMessageFragment_to_navigation_chat)
            }
        }
        _parent?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        //setup view model
        _viewModel = ViewModelProvider(this)[ChatMessageViewModel::class.java]
        _viewModel.invoke(args.chatRoom)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {

        _binding.customToolBar.onStartIconClick = {
            navigateToPage(R.id.action_chatMessageFragment_to_navigation_chat)
        }
        _binding.customToolBar.setTitle(args.user.firstName!!)
        //setting for recycler view
        _recyclerViewAdapter = ChatMessageRecyclerViewAdapter(_parent?.currentUser!!, args.user)
        _recyclerViewAdapter.setItemOrderBy {
            it.sortedBy { message ->
                message.timeStamp
            }
        }
        _recyclerViewAdapter.setOnItemDisplayListener { message ->
            if (message.receiverId == _parent?.currentUser?.id && !message.seen) {
                _viewModel.upsert(
                    message.copy(
                        seen = true
                    )
                )
            }
        }

        // auto scroll down after submit list
        var job: Job? = null
        _recyclerViewAdapter.setOnDataSubmitListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                _binding.rvChatMessageContent.scrollToPosition(_recyclerViewAdapter.itemCount - 1)
            }
        }

        _binding.rvChatMessageContent.apply {
            adapter = _recyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // click send btn
        _binding
        _binding.etChatMessage.apply {
            this.onSendBtnClickListener {
                if (_binding.etChatMessage.getText().trim()
                        .isNotEmpty() || _viewModel.selectedFileList.value!!.size > 0
                ) {
                    _viewModel.sendStatus.value = Resource.Loading()
                    Log.i("upfile", "call fun")
                    val message = Message(
                        timeStamp = System.currentTimeMillis(),
                        content = this.getText(),
                        chatRoomId = args.chatRoom.id,
                        senderId = _parent?.currentUser?.id,
                        receiverId = args.user.id
                    )
                    if (_viewModel.selectedFileList.value != null && _viewModel.selectedFileList.value!!.size > 0) {
                        _viewModel.uploadFile()
                        _viewModel.fileUploaded.observe(viewLifecycleOwner, Observer {
                            Log.i("upfile", "uploaded: ${it.size}")
                            if (it.size == _viewModel.selectedFileList?.value!!.size) {
                                val message2 = message.copy(attachFiles = it.toList())
                                Log.i("upfile", "uploaded: ${it.toString()}")
                                //reset value
                                _viewModel.selectedFileList.value = mutableListOf()
                                this.setText("")
                                //do send
                                _viewModel.upsert(
                                    message2,
                                    requireContext(),
                                    _parent?.currentUser?.firstName,
                                    args.user.id
                                )
                                _viewModel.sendStatus.value = Resource.Success(message2)
                            }
                        })
                    } else {
                        Log.i("upfile", "call fun2")
                        this.setText("")
                        _viewModel.upsert(
                            message,
                            requireContext(),
                            _parent?.currentUser?.firstName,
                            args.user.id
                        )
                    }
                }
            }
        }
        //set up for attach file
        _recyclerViewFileSelected = FileSelectedRecyclerViewAdapter()
        _recyclerViewFileSelected.setOnXBtnClickListener { file ->
            _viewModel.removeFile(file)
        }
        _binding.etChatMessage.binding.rvFileList.apply {
            adapter = _recyclerViewFileSelected
        }

        //set up for take photo
        _binding.etChatMessage.onPhotoBtnClickListener {
            takeImage()
        }

        //set up for take file
        _binding.etChatMessage.onAttachBtnClickListener {
            pickFile()
        }
    }

    fun takeImage() {
        if (imageSelectorHelper.checkUserPermission(requireContext())) {
            takeImageFromCameraOrGallery()
        } else {
            imageSelectorHelper.requestPermission(_parent!!)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ImageSelectorHelper.PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                takeImage()
            } else {
                Log.i("chekc", "permission failed")
            }
        }
    }

    private fun showLoading() {
        _binding.pbChatMessage.visibility = View.VISIBLE
    }

    private fun hindLoading() {
        _binding.pbChatMessage.visibility = View.INVISIBLE
    }

    private fun setupObservers() {
        _viewModel.sendStatus.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    _binding.etChatMessage.binding.ibBtnSend.isEnabled = true
                    hindLoading()
                }
                is Resource.Loading -> {
                    _binding.etChatMessage.binding.ibBtnSend.isEnabled = false
                    showLoading()
                }
                else -> {
                    hindLoading()
                }
            }
        })

        _viewModel.messageList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource?.data?.let {
                        _recyclerViewAdapter.submitList(it)
                    }
                }

                is Resource.Loading -> {
                    Log.i("test", "loading")
                }

                else -> {
                    Log.i("test", "err: ${resource.message}")
                }
            }
        })

        _viewModel.messageSend.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    Log.i("test", "send success")
                }

                is Resource.Loading -> {
                    Log.i("test", "send loading")
                }

                else -> {
                    Log.i("test", "err: ${resource.message}")
                }
            }
        })

        _viewModel.selectedFileList.observe(viewLifecycleOwner, Observer { fileList ->
            _binding.etChatMessage.setDisplayAttachFile(fileList.size > 0)
            _recyclerViewFileSelected.submitList(fileList)
        })
    }

    //image

    val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                try {
                    val fileName = FileHelper.getFileName(imageUri, requireContext())
                    val fileSize = FileHelper.getFileSize(imageUri, requireContext())
                    _viewModel.selectFile(
                        Attachment.TYPE_IMAGE,
                        imageUri.toString(),
                        fileName = fileName,
                        fileSize
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                try {
                    val capturePhoto = data?.extras?.get("data") as Bitmap
                    val imageUri: Uri =
                        imageSelectorHelper.getImageUri(requireContext(), capturePhoto)
                    val fileName = FileHelper.getFileName(imageUri, requireContext())
                    val fileSize = FileHelper.getFileSize(imageUri, requireContext())
                    _viewModel.selectFile(
                        Attachment.TYPE_IMAGE,
                        imageUri.toString(),
                        fileName,
                        fileSize
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    fun takeImageFromCameraOrGallery() {
        val options = arrayOf("Select photo from Gallery", "Capture photo from Camera")
        val title = "Select Photo From"
        AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(options, DialogInterface.OnClickListener { _, option ->
                when (option) {
                    0 -> selectPhotoByGallery()
                    1 -> capturePhotoByCamera()
                }
            }).show()
    }

    private fun capturePhotoByCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun selectPhotoByGallery() {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    //pick file
    val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val fileUri = result.data?.data
                val fileName = FileHelper.getFileName(fileUri, requireContext())
                val fileType = Attachment.getType(fileUri!!, requireContext())
                val fileSize = FileHelper.getFileSize(fileUri, requireContext())
                _viewModel.selectFile(fileType, fileUri.toString(), fileName, fileSize)
            }
        }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/zip",
                "application/x-rar-compressed",
                "video/*",
                "audio/*",
                "text/*"
            )
        )
        pickFileLauncher.launch(intent)
    }
}