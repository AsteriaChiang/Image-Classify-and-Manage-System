package com.photo.photo.controller;

import com.drew.imaging.ImageProcessingException;
import com.photo.photo.entity.Photo;
import com.photo.photo.service.PhotoService;
import com.photo.photo.service.TagService;
import com.photo.photo.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping ("/file")
//@EnableAsync
public class FileController
{
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static String path = PhotoService.getPhotoStorePath ();

    private String th = "th/";


    @Autowired
    private PhotoService photoService;

    @Autowired
    private TagService tagService;

//    @ResponseBody
//    @RequestMapping ("/upload")
//    public Result uploadPhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException
    public Result upload(MultipartFile file, String userId) throws IOException, ImageProcessingException

    {
        Result res = photoService.upload (file, userId);                                                          //处理上传的图片
        if (res.getCode () == 1)
        {
            Photo photo = res.getPhoto ();
            photoService.saveTag (photo, tagService.writeTag (photo.getName (), photo.getPhotoId (), userId));            //通过百度云图像识别获取标签
            ThumbnailsMake.Make (330, 250, path, path + th, photo.getName ());               //生成缩略图
        }
        log.info ("成功上传一张图片！！");
        return res;
    }

    @ResponseBody
    @RequestMapping("/multiUpload")
    public RePhotoInfo multiUploadPhoto(HttpServletRequest request) throws IOException, ImageProcessingException
    {
        log.info ("开始多图上传");
        String sessionId = request.getParameter ("sessionId");
        String message = UserIdFromRequest.getUserId (request, sessionId);
        String userId;
        switch (message)
        {
            case "error, 无session！":
            case "error, session中未能获取userId":
                return (new RePhotoInfo (message));
            default:
                userId = message;

        }
        RePhotoInfo rePhotoInfo = new RePhotoInfo ();
        List<MultipartFile> files = new ArrayList<> ();
        List<Photo> photoRes = new ArrayList<> ();
        Map<String, Object> map = new HashMap<> ();
        while (((MultipartHttpServletRequest) request).getFiles("file").size () > 0 && ((MultipartHttpServletRequest) request).getFiles("file") != files)
        {
            files = ((MultipartHttpServletRequest) request).getFiles("file");
            Result res = upload (files.get (0), userId);
            photoRes.add(res.getPhoto ());
        }
        map.put ("Photos", photoRes);
        rePhotoInfo.setData (map);
        return (new RePhotoInfo ("test"));
    }

    @ResponseBody
    @RequestMapping ("/upload")
    public Result uploadPhoto(@RequestParam ("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException
    {
        String message = UserIdFromRequest.getUserId (request);
        String userId;
        switch (message)
        {
            case "error, 无session！":
            case "error, session中未能获取userId":
                return (new Result (message));
            default:
                userId = message;

        }
        return upload (file, userId);
    }
}
