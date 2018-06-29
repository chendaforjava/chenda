package com.vein.service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vein.bean.Dept;
import com.vein.bean.Sign;
import com.vein.bean.User;
import com.vein.dao.DAO;
import com.vein.util.JsonUtil;
import com.vein.util.POIUtil;
import com.vein.util.SortUtil;

@Service
public class ReportDownLoadService {
	
	
	@Autowired
	DAO dao;
	
	@Autowired
	UserSignService userSignService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public void downLoadCurrentReport(HttpServletRequest request,HttpServletResponse response,String corId){
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			//设置单元格样式
			XSSFCellStyle style = POIUtil.setStyle(workbook, HorizontalAlignment.CENTER, 
					VerticalAlignment.CENTER,POIUtil.setFont(workbook, (short)16, "黑体", false));
			XSSFSheet sheet = workbook.createSheet("点名记录");
			//合并单元格
			POIUtil.mergeCell(sheet, new CellRangeAddress(0, 1, 0, 6));
			
			//生成一个单元格
			XSSFRow row1 = POIUtil.createRow(sheet, 0);
			
			XSSFCell themeCell = row1.createCell(0);
			//设置单元格内容
			POIUtil.cellFilling(themeCell, style, "监狱点名报表");
			
			
			
			//设置表头信息
			XSSFCellStyle style2 = POIUtil.setStyle(workbook, HorizontalAlignment.CENTER, 
					VerticalAlignment.CENTER,POIUtil.setFont(workbook, (short)10, "宋体", false));
			
			XSSFCellStyle style3 = POIUtil.setStyle(workbook, HorizontalAlignment.LEFT, 
					VerticalAlignment.CENTER,POIUtil.setFont(workbook, (short)10, "宋体", false));
			
			List<Date> timeList = getSignRange();
			Date startDate = timeList.get(0);
			Date endDate = timeList.get(1);
			
			POIUtil.mergeCell(sheet, new CellRangeAddress(2, 2, 0, 1));
			POIUtil.mergeCell(sheet, new CellRangeAddress(2, 2, 2, 4));
			XSSFRow startTimeRow = POIUtil.createRow(sheet, 2);
			XSSFCell startTimeCell1 = startTimeRow.createCell(0);
			XSSFCell startTimeCell2 = startTimeRow.createCell(2);
			POIUtil.cellFilling(startTimeCell1, style3, "当前时间:");
			POIUtil.cellFilling(startTimeCell2, style3, sdf2.format(new Date()));
			
			POIUtil.mergeCell(sheet, new CellRangeAddress(3, 3, 0, 1));
			XSSFRow row2 = POIUtil.createRow(sheet, 3);
			XSSFCell themeCell2 = row2.createCell(0);
			POIUtil.cellFilling(themeCell2, style3, "点名开始时间:");
			
			
			XSSFCell startTimeCell = row2.createCell(2);
			POIUtil.cellFilling(startTimeCell, style2, sdf.format(startDate));
			
			
			POIUtil.mergeCell(sheet, new CellRangeAddress(3, 3, 3, 4));
			XSSFCell themeCell3 = row2.createCell(3);
			POIUtil.cellFilling(themeCell3, style3,"点名截止时间:");
			
			
			
			XSSFCell endTimeCell = row2.createCell(5);
			POIUtil.cellFilling(endTimeCell, style2, sdf.format(endDate));
			
			
			String[] titileArray = {"用户ID","姓名","是否在场","点名时间"};
			
			
			XSSFRow tempRow = POIUtil.createRow(sheet, 4);
			for(int i=0;i<4;i++){
				XSSFCell tempCell = tempRow.createCell(i);
				POIUtil.cellFilling(tempCell, style2,titileArray[i]);
			}
			
			String deptName = null;
			String deptId = request.getParameter("deptId");
			if(deptId.equals("0")){
				deptName = "全部";
			}else{
				Dept department = (Dept)dao.selectOne("ReportMapper.selectDeptById", deptId);
				deptName = department.getDeptName();
			}
			
			XSSFCell deptCell = startTimeRow.createCell(5);
			POIUtil.cellFilling(deptCell, style3, "部门:");
			XSSFCell deptNameCell = startTimeRow.createCell(6);
			POIUtil.cellFilling(deptNameCell, style3, deptName);
			
			
			Map<String,Object> userinfoMap = getUserSign(deptId,corId);
			List<User> userSignList = (ArrayList<User>)userinfoMap.get("userSignList");
			List<User> allUserList = (ArrayList<User>)userinfoMap.get("allUserList");
			
			Map<String,User> singUserMap = new HashMap<String,User>();
			for(Iterator<User> itor = userSignList.iterator();itor.hasNext();){
				User user = itor.next();
				String uJobNum = user.getUjobNum();
				singUserMap.put(uJobNum, user);
			}
			for(int i=0;i<allUserList.size();i++){
				XSSFRow temp_Row = POIUtil.createRow(sheet,(5+i));
				User user = allUserList.get(i);
				String uJobNum = user.getUjobNum();
				String name = user.getuName();
				String present = null;
				Date signTime = null;
				User signUser = singUserMap.get(uJobNum);
				if(signUser!=null){
					present = "是";
					signTime = signUser.getSignList().get(0).getSignTime();
				}else{
					present = "否";
				}
				for(int j=0;j<4;j++){
					XSSFCell temp_Cell = temp_Row.createCell(j);
					if(j==0){
						//设置用户ID
						POIUtil.cellFilling(temp_Cell, style2, uJobNum);
					}else if(j==1){
						//设置姓名
						POIUtil.cellFilling(temp_Cell, style2, name);
					}else if(j==2){
						//设置是否到
						POIUtil.cellFilling(temp_Cell, style2, present);
					}else{
						//设置点名时间
						if(signTime==null){
							POIUtil.cellFilling(temp_Cell, style2, "#");
						}else{
							POIUtil.cellFilling(temp_Cell, style2, sdf.format(signTime));
						}
					}
				}
			}
			
			 response.addHeader("Content-Disposition", "attachment;filename=currentReport" + ".xlsx");  
			 response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); 
			 response.setHeader("Pragma", "no-cache");  
		     response.setHeader("Cache-Control", "no-cache");  
		     response.setDateHeader("Expires", 0);  
			 OutputStream out = response.getOutputStream();
			 out.flush();
			 workbook.write(out);
			 out.close();
			 workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void getHistoryReport(HttpServletRequest request,HttpServletResponse response,String corId){
		try {
			String startTime = request.getParameter("startTime");
			Date startDate=sdf2.parse(startTime);
			String endTime = request.getParameter("endTime");
			Date endDate = sdf2.parse(endTime);
			String deptId = request.getParameter("deptId");
			int deptNoTemp = Integer.parseInt(deptId);
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("startDate", startDate);
			parameters.put("endDate", endDate);
			parameters.put("deptNo", deptId);
			parameters.put("deptNoTemp", deptNoTemp);
			long startTimeStamp = startDate.getTime();
			long endTimeStamp = endDate.getTime();
			
			List<String> listTime = new ArrayList<String>();
			
			while(startTimeStamp<=endTimeStamp){
				String tempString = null;
				
				Date tempDate = new Date(startTimeStamp);
				if(startTimeStamp==endTimeStamp && tempDate.getHours()==0 && tempDate.getMinutes()==0){
					tempString = "24"+":"+"00";
				}else{
					String tempHour =tempDate.getHours()<10?"0"+tempDate.getHours():String.valueOf(tempDate.getHours());
					String tempMinute = tempDate.getMinutes()<10?"0"+tempDate.getMinutes():String.valueOf(tempDate.getMinutes());
					tempString = tempHour+":"+tempMinute;
				}
				listTime.add(tempString);
				startTimeStamp +=30*60*1000;
			}
			
			
			List<String> timeRangeList = new ArrayList<String>();
			for(int i=0;i<listTime.size()-1;i++){
				if(i<listTime.size()){
					String timeRange = listTime.get(i)+"-"+listTime.get(i+1);
					timeRangeList.add(timeRange);
				}
			}
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			//设置单元格样式
			XSSFCellStyle style = POIUtil.setStyle(workbook, HorizontalAlignment.CENTER, 
					VerticalAlignment.CENTER,POIUtil.setFont(workbook, (short)16, "黑体", false));
			XSSFSheet sheet = workbook.createSheet("点名记录");
			sheet.setDefaultColumnWidth(11);
			
			//合并单元格
			POIUtil.mergeCell(sheet, new CellRangeAddress(0, 1, 0, 6));
			
			//生成一个单元格
			XSSFRow row1 = POIUtil.createRow(sheet, 0);
			
			XSSFCell themeCell = row1.createCell(0);
			//设置单元格内容
			POIUtil.cellFilling(themeCell, style, "监狱点名报表");
			
			
			//设置表头信息
			XSSFCellStyle style2 = POIUtil.setStyle(workbook, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER,POIUtil.setFont(workbook, (short)10, "宋体", false));
			
			
			XSSFRow totalUserCell = POIUtil.createRow(sheet, 2);
			POIUtil.cellFilling(totalUserCell.createCell(1), style2, "应到人数:");
			XSSFRow actualUserCell = POIUtil.createRow(sheet, 3);
			POIUtil.cellFilling(actualUserCell.createCell(1), style2, "实到人数:");
			//生成一个单元格
			XSSFRow row4 = POIUtil.createRow(sheet, 4);
			
			POIUtil.cellFilling(row4.createCell(0), style2, "用户ID");
			POIUtil.cellFilling(row4.createCell(1), style2, "姓名");
			for(int i=0;i<timeRangeList.size();i++){
				XSSFCell tempCell = row4.createCell(i+2);
				POIUtil.cellFilling(tempCell, style2, timeRangeList.get(i));
			}
			
			
			
			String deptName = null;
			if(deptId.equals("0")){
				deptName = "全部";
			}else{
				Dept department = (Dept)dao.selectOne("ReportMapper.selectDeptById", deptId);
				deptName = department.getDeptName();
			}
			
			POIUtil.cellFilling(totalUserCell.createCell(0), style2, "部门");
			POIUtil.cellFilling(actualUserCell.createCell(0), style2, deptName);
			
			
			
			//该部门下全部的员工
			List<User> allUserList = (ArrayList<User>)dao.findList("ReportMapper.selectalluser",parameters);
			//该部门下在该时段签到的人
			List<User> userSignList = (ArrayList<User>)dao.findList("ReportMapper.selectSignList",parameters);
			
			
			//应到人数
			int allUserNum = allUserList.size();
			
			POIUtil.cellFilling(totalUserCell.createCell(2), style2, String.valueOf(allUserNum));
			
			
			
			
			Map<String,User> userSignMap = new HashMap<String,User>();
			for(User user :userSignList){
				userSignMap.put(user.getUjobNum(), user);
			}
			
			
			
			for(int i=0;i<allUserList.size();i++){
				User tempUser = allUserList.get(i);
				XSSFRow usertempRow = POIUtil.createRow(sheet, 5+i);
				POIUtil.cellFilling(usertempRow.createCell(0), style2,tempUser.getUjobNum());
				POIUtil.cellFilling(usertempRow.createCell(1), style2, tempUser.getuName());
				User signedUser = userSignMap.get(tempUser.getUjobNum());
				for(int j=0;j<timeRangeList.size();j++){
					//现将所有单元格设置成未到人员
					POIUtil.cellFilling(usertempRow.createCell(2+j), style2, "未到");
				}
				if(signedUser!=null){
					//说明在该时段有签名记录
					List<Sign> signList = signedUser.getSignList();
					for(int k=0;k<signList.size();k++){
						Date signTime = signList.get(k).getSignTime();
						String signTimeTemp = signTime.getHours()+":"+signTime.getMinutes();
						listTime.add(signTimeTemp);
						SortUtil sortUtil = new SortUtil();
						Collections.sort(listTime, sortUtil);
						int indexNum = listTime.indexOf(signTimeTemp);
						POIUtil.cellFilling(usertempRow.createCell(1+indexNum), style2,"已到");
						listTime.remove(signTimeTemp);
					}
				}
			}
			
			
			for(int i=0;i<timeRangeList.size();i++){
				String timeRange = timeRangeList.get(i);
				String[] timeArray = timeRange.split("-");
				String time1 = timeArray[0];
				int startTimeHour = Integer.valueOf(time1.split(":")[0]);
				int startTimeMin = Integer.valueOf(time1.split(":")[1]);
				String time2 = timeArray[1];
				int endTimeHour = Integer.valueOf(time2.split(":")[0]);
				int endTimeMin = Integer.valueOf(time2.split(":")[1]);
				startDate.setHours(startTimeHour);
				startDate.setMinutes(startTimeMin);
				endDate.setHours(endTimeHour);
				endDate.setMinutes(endTimeMin);
				parameters.put("startDate", startDate);
				parameters.put("endDate", endDate);
				System.out.println(JsonUtil.obj2json(parameters));
				List<User> userSignListTemp = (ArrayList<User>)dao.findList("ReportMapper.selectSignList",parameters);
				int signedUserNum = userSignListTemp.size();
				System.out.println(signedUserNum);
				POIUtil.cellFilling(actualUserCell.createCell(2+i), style2, String.valueOf(signedUserNum));
			}
			
			 response.addHeader("Content-Disposition", "attachment;filename=historyReport" + ".xlsx");
			 response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			 response.setHeader("Pragma", "no-cache");
		     response.setHeader("Cache-Control", "no-cache");
		     response.setDateHeader("Expires", 0);
			 OutputStream out = response.getOutputStream();
			 out.flush();
			 workbook.write(out);
			 out.close();
			 workbook.close();
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public List<Date> getSignRange(){
		List<Date> dateList = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		Date startDate = null;
		Date endDate = null;
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		if (minute < 30) {
			minute = 29;
			second = 59;
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = calendar.getTime();
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			endDate = calendar.getTime();
		} else {
			minute = 59;
			second = 59;
			calendar.set(Calendar.MINUTE, 30);
			calendar.set(Calendar.SECOND, 0);
			startDate = calendar.getTime();
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			endDate = calendar.getTime();
		}
		dateList.add(startDate);
		dateList.add(endDate);
		return dateList;
	}
	
	
	public Map<String,Object> getUserSign(String deptId,String corId){
		Map<String,Object> parameters = new HashMap<String,Object>();
		List<Date> dateList = getSignRange();
		Date startDate = dateList.get(0);
		Date endDate  = dateList.get(1);
		parameters.put("startDate", startDate);
		parameters.put("endDate", endDate);
		parameters.put("deptId", deptId);
		parameters.put("corId", corId);
		List<User> userSignList = (ArrayList<User>)dao.findList("UserSignMapper.getAllSignUSerList", parameters);
		List<User> allUserList = (ArrayList<User>)dao.findList("UserSignMapper.getUserInfo", parameters);
		parameters.clear();
		parameters.put("userSignList", userSignList);
		parameters.put("allUserList", allUserList);
		return parameters;
	}
	
}
