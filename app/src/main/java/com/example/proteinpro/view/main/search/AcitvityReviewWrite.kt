package com.example.proteinpro.view.main.search

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityAcitvityReviewWriteBinding
import com.example.proteinpro.util.Class.food.ReviewItem
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.RecyclerView.PhotoListAdapter
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.Retrofit.ServerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutionException


class AcitvityReviewWrite : AppCompatActivity() {

    private lateinit var realUri : Uri
    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
    ActivityAcitvityReviewWriteBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    private val PICK_IMAGE_FROM_GALLERY = 1000
    private val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010
    private val REQUEST_IMAGE_CODE = 101

    private var imageList = ArrayList<Uri>()
    private lateinit var mAdapter : PhotoListAdapter

    // 유틸 클래스
    private lateinit var foodRetrofitHelper: FoodRetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper

    //
    private lateinit var item: ReviewItem
    private lateinit var itemId : String
    private lateinit var name : String
    private lateinit var editType : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acitvity_review_write)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityAcitvityReviewWriteBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        itemId = intent.getStringExtra("ItemId").toString()
        name = intent.getStringExtra("name").toString()
        editType = intent.getStringExtra("editType").toString()

        initUtils()
        initViews()
        initListener()
        initData()

    }

    private fun initData() {


    }

    private fun initViews(){
        // 뷰 초기화

        mAdapter  = PhotoListAdapter(this, imageList, "write")

        binding.photoRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.photoRV.adapter = mAdapter

        if(editType == "update"){// 업데이트 인 경우
            // 상단 등록 버튼 표기 변경
            binding.submitTV.setText("수정완료")

            val token = preferenceHelper.get_jwt_Token()
            if(token!=null){

                foodRetrofitHelper.getReviewDetails(token, itemId.toString(), object : FoodRetrofitHelper.reviewDataCallback{
                    override fun onSuccess(reviewItem: ReviewItem) {

                        item = reviewItem

                        Log.i ("getReviewDetails", "ReviewItem"+reviewItem)


                            binding.goodET.setText(reviewItem.긍정후기)
                            binding.badET.setText(reviewItem.부정후기)
                            binding.reviewRB.rating = reviewItem.평점.toFloat()
                            // 이미지는 어떻게 하지?
                        lifecycleScope.launch(Dispatchers.Main) {
                            imageList = convertUrlsToUris(this@AcitvityReviewWrite, reviewItem.이미지주소)

                            mAdapter.setItem_list(imageList)
                        }



                    }

                    override fun onFailure() {
                        Log.i ("getReviewDetails", "onFailure")
                    }

                })
            }

        }else{

        }

    }
    private fun initListener(){
        // 리스너 초기화

        binding.addPhotoIB.setOnClickListener {
            showImageSourcePopup()
        }

        mAdapter.setOnItemClickListener(object : PhotoListAdapter.OnItemClickListener {
            override fun onCancleClick(v: View?, position: Int, uri: Uri) {

                imageList.removeAt(position)
                mAdapter.notifyItemRemoved(position)

            }

        })

        binding.submitTV.setOnClickListener {

            val good = binding.goodET.text.toString()
            val bad =binding.badET.text.toString()
            val star = binding.reviewRB.rating.toInt()
            val token = preferenceHelper.get_jwt_Token()
            val key = intent.getIntExtra("foodKey" ,-1).toString()
            val imageFileList = urisToImageFiles(this, imageList)
            Log.i ("정보태그", "imageFileList: "+ imageFileList.get(0))


            val imagePartlist = imageFilesToMultipartParts(imageFileList)

            Log.i ("imagePartlist", "imagePartlist: "+ imagePartlist.get(0))
            /***
             * isChange 의 경우 수정으로 온 경우 처음 받은 이미지 리스트 상태를 비교해서 받아오도록 하자
             * (TODO)
             * 두 어레이리스트를 constrainsAll() 메서드 이용해서 같은지 확인하자
             * 같으면 false
             * 다르면 true
             */
            val isChange = true

            if (token != null) {

                if(editType == "update"){

                    item.긍정후기 =good
                    item.부정후기 = bad
                    item.평점 = star


                    foodRetrofitHelper.updateReviews(item,isChange, token, imagePartlist){

                        if(it){
                            // 수정 완료
                            finish()
                        }else{
                            Toast.makeText(getApplicationContext(), "수정에 실패했습니다. 다시 시도해 주세요",Toast.LENGTH_SHORT).show()
                        }

                    }

                }else{
                    foodRetrofitHelper.submitReview(good,bad,star,key,token,imagePartlist){
                        if(it){
                            // 등록 성공
                            finish()
                        }else{
                            // 등록 실패
                            Toast.makeText(getApplicationContext(), "등록에 실패했습니다. 다시 시도해 주세요",Toast.LENGTH_SHORT).show()
                        }

                    }

                }

            }

        }



    }
    private fun initUtils(){
        // 유틸 클래스 초기화

        preferenceHelper = PreferenceHelper(this)
        foodRetrofitHelper = FoodRetrofitHelper(this)

    }

    private fun showGallery(activity: Activity, extra: String) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(extra, true)
        activity.startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
    }

    //사진찍기
    private fun takePicture(activity: Activity) {
        val imageTakeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.i ("takePicture", ""+imageTakeIntent.resolveActivity(packageManager))

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            // MediaStore.EXTRA_OUTPUT을 Key로 하여 Uri를 넘겨주면
            // 일반적인 Camera App은 이를 받아 내가 지정한 경로에 사진을 찍어서 저장시킨다.
            imageTakeIntent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            activity.startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE)

//            activity.startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE)
        }


    }


    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_FROM_GALLERY_PERMISSION)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private fun showCameraPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("카메라 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_IMAGE_CODE)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    // 사진 선택(갤러리에서 나온) 이후 실행되는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            data?.let { it ->
                if (it.clipData != null) {   // 사진을 여러개 선택한 경우

                    val count = it.clipData!!.itemCount+ imageList.size
                    Log.i ("정보태그", "it.clipData: "+ it.clipData+" count: "+ count)

                    if (count > 5) {
                        Toast.makeText(this@AcitvityReviewWrite, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    for (i in 0 until it.clipData!!.itemCount) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        imageList.add(imageUri)
                    }
                } else {      // 1장 선택한 경우
                    val count = it.clipData!!.itemCount+ imageList.size
                    if (count > 5) {
                        Toast.makeText(this@AcitvityReviewWrite, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    val imageUri = it.data!!
                    imageList.add(imageUri)
                }
            }
            Log.i ("정보태그", "imageList")
            mAdapter.setItem_list(imageList)
        }

        if (requestCode === REQUEST_IMAGE_CODE && resultCode === RESULT_OK) {

            when (requestCode) {
                REQUEST_IMAGE_CODE -> {
                    val count = imageList.size+1
                    if(count > 5) {
                        Toast.makeText(this@AcitvityReviewWrite, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }else{
                        realUri?.let { uri ->
                            imageList.add(uri)
                        }
                    }

                }
            }
            mAdapter.setItem_list(imageList)
        }

    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PICK_IMAGE_FROM_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showGallery(this@AcitvityReviewWrite, Intent.EXTRA_ALLOW_MULTIPLE)
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun showImageSourcePopup() {
        val options = arrayOf("갤러리에서 선택", "카메라로 촬영", "취소")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("이미지 선택")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    when {
                        // 갤러리 접근 권한이 있는 겨우
                        ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED -> showGallery(this@AcitvityReviewWrite,
                            Intent.EXTRA_ALLOW_MULTIPLE)

                        // 갤러리 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                        shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        -> showPermissionContextPopup()

                        // 권한 요청 하기
                        else -> requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            PICK_IMAGE_FROM_GALLERY_PERMISSION)
                    }

                }
                1 -> {

                    when {
                        // 카메라로 촬영
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA
                        )== PackageManager.PERMISSION_GRANTED ->
                        {  Log.i ("takePicture", "takePicture")
                            takePicture(this@AcitvityReviewWrite)
                        }
                        // 카메라 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                        shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                        -> showCameraPermissionContextPopup()

                        // 카메라 권한 요청하기
                        else -> {Log.i ("else", "else")
                        requestPermissions(
                            arrayOf(android.Manifest.permission.CAMERA),
                            REQUEST_IMAGE_CODE
                        )}

                    }

                }
                // 2일 경우는 "취소" 옵션이므로 아무 작업도 하지 않습니다.
            }
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

        private fun createImageUri(filename: String, mimeType: String): Uri? {
            var values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            return this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }

    private fun newFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }



    private fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        val fileName = "image.${MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))}"
        val tempFile = File(context.cacheDir, fileName)

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun convertImageNamesToURIs(context: Context, imageNames: ArrayList<String>): ArrayList<Uri> {
        val uriList = ArrayList<Uri>()

        for (imageName in imageNames) {
            val uri = Uri.Builder()
                .scheme("android.resource")
                .authority(context.packageName)
                .appendPath("drawable")
                .appendPath(imageName) // 이미지 파일 이름을 여기에 추가
                .build()

            uriList.add(uri)
        }

        return uriList
    }

    // Uri 목록을 이미지 파일 목록으로 변환하는 함수
    fun urisToImageFiles(context: Context, uris: List<Uri>): List<File> {
        val imageFiles = mutableListOf<File>()

        for (uri in uris) {
            try {
                // Uri에서 InputStream 열기
                val inputStream = context.contentResolver.openInputStream(uri)

                if (inputStream != null) {
                    // 이미지 파일을 저장할 임시 파일 생성
                    val file = createImageFile(context)

                    // 파일에 데이터 복사
                    val outputStream = FileOutputStream(file)
                    val buffer = ByteArray(1024)
                    var read: Int

                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }

                    outputStream.close()
                    inputStream.close()

                    // 이미지 파일 목록에 추가
                    imageFiles.add(file)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return imageFiles
    }

    // 이미지 파일을 저장할 디렉터리에 임시 파일 생성
    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // 임시 파일 생성
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    // 이미지 파일 목록을 MultipartBody.Part 목록으로 변환하는 함수
    fun imageFilesToMultipartParts(imageFiles: List<File>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()

        for (file in imageFiles) {
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val part = MultipartBody.Part.createFormData("files", file.name, requestFile)
            parts.add(part)
        }

        return parts
    }

    // URL 목록을 받아서 Uri 목록으로 변환하는 함수
    suspend fun convertUrlsToUris(context: Context, imageUrls: List<String>): ArrayList<Uri> {
        val uris = ArrayList<Uri>()

        for (imageUrl in imageUrls) {
            val uri = downloadImageAndSaveToFile(ServerData.img_URL_review + imageUrl, context)
            uri?.let { uris.add(it) }
        }

        return uris
    }


    //이미지 다운 후 로컬에 저장하고 해당 이미지 URI 반환
    suspend fun downloadImageAndSaveToFile(imageUrl: String, context: Context): Uri? {
        try {
            // Glide 작업을 백그라운드 스레드에서 실행
            return withContext(Dispatchers.IO) {
                val options = RequestOptions().override(Target.SIZE_ORIGINAL)
                val futureTarget = Glide.with(context)
                    .asFile()
                    .load(imageUrl)
                    .apply(options)
                    .submit()
                    .get()

                val imageFile = futureTarget as File

                // 이미지 파일의 Uri를 반환
                return@withContext Uri.fromFile(imageFile)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return null
    }


}
