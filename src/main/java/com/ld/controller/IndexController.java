/**  
 * Copyright © 2018LD. All rights reserved.
 *
 * @Title: IndexController.java
 * @Prject: videoadmin
 * @Package: com.ld.controller
 * @Description: TODO
 * @author: Myzhang  
 * @date: 2018年7月4日 下午6:33:16
 * @version: V1.0  
 */
package com.ld.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * @ClassName: IndexController
 * @Description: TODO
 * @author: Myzhang  
 * @date: 2018年7月4日 下午6:33:16
 */
public class IndexController {
	
	@FXML
	JFXListView<String> fileList;
	@FXML
	ImageView imageView_1;
	@FXML
	ImageView imageView_2;
	@FXML
	ImageView imageView_3;
	@FXML
	ImageView imageView_4;
	
	private int k=0;//计数器
	static final String video = "mp4,3gp,avi,mkv,wmv,mpg,vob,flv,swf,mov,xv,rmvb";
	
	private String sourceFile = "D:\\aaaaaa";
	
	private static List<File> dataList = new ArrayList<>();
	public void queryList() {
		dataList.clear();
		k=0;
		getListFile(new File(sourceFile), dataList);
		ObservableList<String> list = FXCollections.observableArrayList();	
		for (File file : dataList) {
			list.add(file.getName());
		}
		fileList.setItems(list);
	}
	
	
	
	public void getListFile(File file,List<File> dataList) {
		if(file.getPath().equals(System.getenv("windir"))) return ;
		File[] listFiles = file.listFiles();
		if(listFiles == null) return;
		for(int i=0;i<listFiles.length;i++) {
			File f = listFiles[i];
			if(!f.isDirectory()) {//文件
				String name = f.getName();
				String[] split = name.split("\\.");
				if(split.length > 1) {
					String type = name.substring(name.lastIndexOf(".") + 1);
					System.out.println("扫描:"+name);
					for(String t:video.split(",")) {//必须是视频类型
						if(t.toLowerCase().equals(type)) {
							System.out.println("匹配的:"+name);
							dataList.add(f);
							break;
						}
					}
				}
			}
			getListFile(f,dataList);
		}
	}

}
