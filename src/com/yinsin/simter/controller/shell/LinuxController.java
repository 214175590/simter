package com.yinsin.simter.controller.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yinsin.simter.controller.BaseController;
import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.frame.Response;
import com.yinsin.simter.frame.security.DesJS;
import com.yinsin.simter.modal.base.PageImpl;
import com.yinsin.simter.modal.shell.LinuxOs;
import com.yinsin.simter.modal.yxos.YxosLinuxHost;
import com.yinsin.simter.modal.yxos.YxosUser;
import com.yinsin.simter.service.yxos.YxosLinuxHostServiceImpl;
import com.yinsin.utils.CommonUtils;
import com.yinsin.utils.DateUtils;
import com.yinsin.utils.FileUtils;
import com.yisin.commun.modal.DataPackage;
import com.yisin.ssh2.jsch.ChannelSftp.LsEntry;
import com.yisin.ssh2.jsch.SftpProgressMonitor;

@Controller
@RequestMapping("/linux")
@ResponseBody
public class LinuxController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LinuxController.class);
	
	@Resource(name="yxosLinuxHostServiceImpl")
    YxosLinuxHostServiceImpl yxosLinuxHostServiceImpl;

	@RequestMapping("/isLogin")
	public Response isLogin(@RequestParam("jsonValue") String jsonValue) throws IOException {
		Response result = new Response();
		try {
			YxosUser user = getYxosUser();
			if (user != null) {
				JSONObject json = parseJsonValue(jsonValue);
				String serverIp = json.getString("serverIp");
				if (serverIp != null) {
					ShellInstance instance = ShellManager.getByIp(user.getUserId(), serverIp);
					if (null != instance) {
						result.success().setDataToRtn(instance.getAccount());
					}
				}
			}
		} catch (Exception e) {
			logger.error("登录失败：" + e.getMessage(), e);
			result.fail("登录失败：" + e.getMessage());
		}
		return result;
	}

	@RequestMapping("/{host}/{port}")
	public Response connection(@PathVariable String host, @PathVariable int port) throws IOException {
		Response result = new Response();
		try {
			YxosUser user = getYxosUser();
			if (user != null) {
				String serverIp = request.getParameter("serverIp");
				String username = request.getParameter("acc");
				String password = request.getParameter("pwd");
				if (username != null && password != null) {
					ShellInstance instance = ShellManager.getByIp(user.getUserId(), serverIp);
					if (null == instance) {
						instance = new ShellInstance();
						instance.setUser(user);
						instance.setUserId(user.getUserId());
						ShellManager.add(user.getUserId(), instance);
					}
					if (instance.getLinuxOs() == null) {
						LinuxOs os = new LinuxOs();
						Argument arg = os.connection(host, username, password, port);
						result.setCode(arg.getCode());
						result.setMessage(arg.getMessage());
						if (result.isSuccess()) {
							
							try {
								YxosLinuxHost linux = new YxosLinuxHost();
								linux.setUserId(user.getUserId());
								linux.setHost(serverIp);
								arg.setObj(linux);
								arg = yxosLinuxHostServiceImpl.select(arg);
								if (arg.isSuccess()) {
									List<YxosLinuxHost> dataList = (List<YxosLinuxHost>) arg.getDataForRtn();
									if (null != dataList && dataList.size() > 0) {
										linux = dataList.get(0);
										linux.setAccount(username);
										linux.setPassword(DesJS.encrypt(password, user.getUserId()));
										linux.setPort(port);
										arg.setObj(linux);
										yxosLinuxHostServiceImpl.update(arg);
									}
								}
							} catch (Exception e) {
							}
							
							instance.setServerIp(serverIp);
							instance.setLinuxOs(os);
							instance.setAccount(username);
							instance.start();

							result.success();
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("登录失败：" + e.getMessage(), e);
			result.fail("登录失败：" + e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/exit")
	public Response exit(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		try {
			YxosUser user = getYxosUser();
			if (user != null) {
				JSONObject json = parseJsonValue(jsonValue);
				String serverIp = json.getString("serverIp");
				ShellInstance instance = ShellManager.getByIp(user.getUserId(), serverIp);
				if (null != instance) {
					instance.setServerIp(null);
					instance.setAccount(null);
					instance.closeLinuxOs();
				}
			}
		} catch (Exception e) {
			logger.error("断开失败：" + e.getMessage(), e);
			result.fail("断开失败：" + e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/load")
	public Response load(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		JSONObject json = parseJsonValue(jsonValue);
		String serverIp = json.getString("serverIp");
		if (null != user && serverIp != null) {
			ShellInstance instance = ShellManager.getByIp(user.getUserId(), serverIp);
			if (null != instance) {
				String param = "path=/home/" + instance.getAccount() + "&account=" + instance.getAccount() + "&serverIp=" + serverIp;

				JSONArray data = new JSONArray();
				JSONObject icon = new JSONObject();
				icon.put("id", CommonUtils.getUUID());
				icon.put("name", "应用程序");
				icon.put("title", "我的电脑");
				icon.put("windowWidth", 1200);
				icon.put("windowHeight", 750);
				icon.put("location", "apps/shell/explorer.html?" + param);
				icon.put("sleft", 10);
				icon.put("top", 10);
				icon.put("isDrag", "true");
				icon.put("isShow", "true");
				icon.put("icon", "images/icons/wpic_folder.png");
				icon.put("levels", 1);
				icon.put("hosts", 1);
				icon.put("types", "exe");
				icon.put("needClose", "true");
				icon.put("needMinimize", "true");
				icon.put("needMaximize", "true");
				icon.put("closeFunction", "");
				icon.put("minFunction", "");
				icon.put("maxFunction", "");
				icon.put("status", 1);
				icon.put("createTime", DateUtils.format());
				icon.put("belong", user.getUserAccount());
				data.add(icon);

				icon = new JSONObject();
				icon.put("id", CommonUtils.getUUID());
				icon.put("name", "应用程序");
				icon.put("title", "控制台");
				icon.put("location", "apps/shell/terminal.html?" + param);
				icon.put("windowWidth", 1200);
				icon.put("windowHeight", 750);
				icon.put("sleft", 10);
				icon.put("top", 10);
				icon.put("types", "exe");
				icon.put("isDrag", "true");
				icon.put("isShow", "true");
				icon.put("icon", "images/icons/terminal.png");
				icon.put("levels", 1);
				icon.put("hosts", 1);
				icon.put("needClose", "true");
				icon.put("needMinimize", "true");
				icon.put("needMaximize", "true");
				icon.put("closeFunction", "");
				icon.put("minFunction", "");
				icon.put("maxFunction", "");
				icon.put("status", 1);
				icon.put("createTime", DateUtils.format());
				icon.put("belong", user.getUserAccount());
				data.add(icon);

				icon = new JSONObject();
				icon.put("id", CommonUtils.getUUID());
				icon.put("name", "应用程序");
				icon.put("title", "在线音乐");
				icon.put("location", "http://web.kugou.com/?" + param);
				icon.put("windowWidth", 745);
				icon.put("windowHeight", 545);
				icon.put("sleft", 10);
				icon.put("top", 10);
				icon.put("types", "exe");
				icon.put("isDrag", "true");
				icon.put("isShow", "true");
				icon.put("icon", "images/icons/i05.png");
				icon.put("levels", 1);
				icon.put("hosts", 0);
				icon.put("needClose", "true");
				icon.put("needMinimize", "true");
				icon.put("needMaximize", "true");
				icon.put("closeFunction", "");
				icon.put("minFunction", "");
				icon.put("maxFunction", "");
				icon.put("status", 1);
				icon.put("createTime", DateUtils.format());
				icon.put("belong", user.getUserAccount());
				data.add(icon);
				
				icon = new JSONObject();
				icon.put("id", CommonUtils.getUUID());
				icon.put("name", "应用程序");
				icon.put("title", "隐心影视");
				icon.put("location", "http://v.yinsin.net/?" + param);
				icon.put("windowWidth", 1200);
				icon.put("windowHeight", 760);
				icon.put("sleft", 10);
				icon.put("top", 10);
				icon.put("types", "exe");
				icon.put("isDrag", "true");
				icon.put("isShow", "true");
				icon.put("icon", "images/icons/g04.png");
				icon.put("levels", 1);
				icon.put("hosts", 0);
				icon.put("needClose", "true");
				icon.put("needMinimize", "true");
				icon.put("needMaximize", "true");
				icon.put("closeFunction", "");
				icon.put("minFunction", "");
				icon.put("maxFunction", "");
				icon.put("status", 1);
				icon.put("createTime", DateUtils.format());
				icon.put("belong", user.getUserAccount());
				data.add(icon);
				
				icon = new JSONObject();
				icon.put("id", CommonUtils.getUUID());
				icon.put("name", "应用程序");
				icon.put("title", "Runner");
				icon.put("location", "other/game/runner/index.html?" + param);
				icon.put("windowWidth", 1200);
				icon.put("windowHeight", 760);
				icon.put("sleft", 10);
				icon.put("top", 10);
				icon.put("types", "exe");
				icon.put("isDrag", "true");
				icon.put("isShow", "true");
				icon.put("icon", "images/icons/g05.png");
				icon.put("levels", 1);
				icon.put("hosts", 1);
				icon.put("needClose", "true");
				icon.put("needMinimize", "true");
				icon.put("needMaximize", "true");
				icon.put("closeFunction", "");
				icon.put("minFunction", "");
				icon.put("maxFunction", "");
				icon.put("status", 1);
				icon.put("createTime", DateUtils.format());
				icon.put("belong", user.getUserAccount());
				data.add(icon);
				
				icon = new JSONObject();
				icon.put("id", CommonUtils.getUUID());
				icon.put("name", "应用程序");
				icon.put("title", "五子棋");
				icon.put("location", "other/game/gobang/index.html?" + param);
				icon.put("windowWidth", 1200);
				icon.put("windowHeight", 760);
				icon.put("sleft", 10);
				icon.put("top", 10);
				icon.put("types", "exe");
				icon.put("isDrag", "true");
				icon.put("isShow", "true");
				icon.put("icon", "images/icons/g06.png");
				icon.put("levels", 1);
				icon.put("hosts", 1);
				icon.put("needClose", "true");
				icon.put("needMinimize", "true");
				icon.put("needMaximize", "true");
				icon.put("closeFunction", "");
				icon.put("minFunction", "");
				icon.put("maxFunction", "");
				icon.put("status", 1);
				icon.put("createTime", DateUtils.format());
				icon.put("belong", user.getUserAccount());
				data.add(icon);

				result.success().setDataToRtn(data);
			}
		}
		return result;
	}

	@RequestMapping("/loadFiles")
	public Response loadFiles(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			JSONObject json = parseJsonValue(jsonValue);
			String serverIp = json.getString("serverIp");
			String path = json.getString("path");
			ShellInstance shell = ShellManager.getByIp(user.getUserId(), serverIp);
			if (null != shell) {
				Vector<LsEntry> files = shell.getLinuxOs().getSftp().ls(path);
				int size = json.getIntValue("_pageSize");
				int index = json.getIntValue("_pageIndex");
				if(size == 0){
					result.success().setDataToRtn(files);
				} else {
					PageImpl page = new PageImpl();
					if(files != null && files.size() > 2){
						Vector<LsEntry> datas = new Vector<LsEntry>();
						int start = index * size + 2,
								end = start + size;
						if(end > files.size()){
							end = files.size();
						}
						
						datas.add(files.get(1));
						for(int i = start; i < end; i++){
							datas.add(files.get(i));
						}
						page.setContent(datas);
						page.setFirst(start == 2);
						page.setLast(end >= files.size());
						page.setNumber(index);
						page.setNumberOfElements(datas.size() - 1);
						page.setSize(size);
						page.setTotalElements(files.size() - 1);
						page.setTotalPages((int)(page.getTotalElements()/size) + (page.getTotalElements()%size > 0 ? 1 : 0));
						result.success().setDataToRtn(page);
					} else {
						page.setContent(new ArrayList<LsEntry>());
						page.setFirst(true);
						page.setLast(true);
						page.setNumber(0);
						page.setNumberOfElements(0);
						page.setSize(size);
						page.setTotalElements(0);
						page.setTotalPages(0);
						result.success().setDataToRtn(page);
					}
				}
			}
		}
		return result;
	}

	@RequestMapping("/shell")
	public Response shell(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			JSONObject json = parseJsonValue(jsonValue);
			String serverIp = json.getString("serverIp");
			String command = json.getString("shell");
			ShellInstance shell = ShellManager.getByIp(user.getUserId(), serverIp);
			try {
				if (null != shell) {
					String s = new String(new byte[] { -62, -96 });
					command = command.replaceAll(s, " ");
					logger.debug("run shell==>" + command);
					if(shell.isSended()){
						shell.getLinuxOs().getChannel().send("\r\n");
					}
					shell.setSended(true);
					shell.getLinuxOs().getChannel().send(command);
				}
			} catch (Exception e) {
			}
			result.success();
		}
		return result;
	}

	@RequestMapping("/editfile")
	public Response editfile(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			JSONObject json = parseJsonValue(jsonValue);
			String serverIp = json.getString("serverIp");
			String path = json.getString("path");
			String name = json.getString("name");
			final ShellInstance shell = ShellManager.getByIp(user.getUserId(), serverIp);
			try {
				if (null != shell) {
					String s = new String(new byte[] { -62, -96 });
					path = path.replaceAll(s, " ");
					name = name.replaceAll(s, " ");
					final String filename = path + name;
					final String dispath = Utils.getDir(path, serverIp) + name;
					final DataPackage dataPackage = new DataPackage();
					dataPackage.setReq("linux.down");
					dataPackage.setFlag(serverIp);
					shell.getLinuxOs().getSftp().get(path + name, dispath, new SftpProgressMonitor() {
						private long max = 0;

						public void init(int op, String src, String dest, long max) {
							shell.setEnable(false);
							dataPackage.setData("{\"type\":\"start\",\"value\":\"" + filename + "\"}");
							send();
							this.max = max;
						}

						public boolean count(long count) {
							dataPackage.setData("{\"type\":\"trans\",\"value\":" + ((float) count / (float) max) + "}");
							send();
							return true;
						}

						public void end() {
							dataPackage.setData("{\"type\":\"editfile\",\"path\":\"" + filename + "\",\"value\":\"" + dispath + "\"}");
							send();
							shell.setEnable(true);
						}

						private void send() {
							try {
								shell.getClient().send(dataPackage);
							} catch (Exception e) {
							}
						}
					});
				}
				result.success();
			} catch (Exception e) {
				result.fail("异常：" + e.getMessage());
			}
		}
		return result;
	}

	@RequestMapping("/getFileContent")
	public Response getFileContent(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			JSONObject json = parseJsonValue(jsonValue);
			String filepath = json.getString("filepath");
			File file = new File(filepath);
			if (file.exists()) {
				try {
					String content = FileUtils.readFileAsString(file);
					result.success().setDataToRtn(content);
				} catch (IOException e) {
					result.fail("Error: " + e.getMessage());
				}
			}
		}
		return result;
	}

	@RequestMapping("/download")
	public Response download(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			JSONObject json = parseJsonValue(jsonValue);
			String serverIp = json.getString("serverIp");
			String path = json.getString("path");
			String name = json.getString("name");
			final ShellInstance shell = ShellManager.getByIp(user.getUserId(), serverIp);
			try {
				if (null != shell) {
					String s = new String(new byte[] { -62, -96 });
					path = path.replaceAll(s, " ");
					name = name.replaceAll(s, " ");
					final String filename = path + name;
					final String dispath = Utils.getDir(path, serverIp) + name;
					final DataPackage dataPackage = new DataPackage();
					dataPackage.setReq("linux.down");
					dataPackage.setFlag(serverIp);
					SftpTransfileMonitor monitor = new SftpTransfileMonitor() {
						private long max = 0;

						public void init(int op, String src, String dest, long max) {
							shell.setEnable(false);
							dataPackage.setData("{\"type\":\"download\",\"value\":\"" + filename + "\"}");
							send();
							this.max = max;
						}

						public boolean count(long count) {
							dataPackage.setData("{\"type\":\"trans\",\"value\":" + ((float) count / (float) max) + "}");
							send();
							return true;
						}

						public void end() {
							dataPackage.setData("{\"type\":\"finish\",\"path\":\"" + filename + "\",\"value\":\"" + dispath + "\"}");
							send();
							shell.setEnable(true);
							super.end();
						}

						private void send() {
							try {
								shell.getClient().send(dataPackage);
							} catch (Exception e) {
							}
						}
					};
					shell.getLinuxOs().getSftp().get(path + name, dispath, monitor);

					while (!monitor.isFinished()) {
						Thread.sleep(10);
					}
					result.success().setDataToRtn(dispath);
				}
			} catch (Exception e) {
				logger.error("异常：" + e.getMessage());
				result.fail("下载失败：" + e.getMessage());
			}
		}
		return result;
	}

	@RequestMapping("/downfile")
	public void downfile(@RequestParam("jsonValue") String jsonValue) {
		YxosUser user = getYxosUser();
		if (null != user) {
			try {
				JSONObject json = parseJsonValue(jsonValue);
				String name = json.getString("name");
				String path = json.getString("path");
				// 下载本地文件
				String fileName = name; // 文件的默认保存名
				// 读到流中
				InputStream inStream = new FileInputStream(path);// 文件的存放路径
				// 设置输出的格式
				response.reset();
				response.setContentType("bin");
				response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
				// 循环取出流中的数据
				byte[] b = new byte[1024];
				int len;
				while ((len = inStream.read(b)) > 0){
					response.getOutputStream().write(b, 0, len);
				}
				inStream.close();
			} catch (Exception e) {
				logger.error("下载异常：" + e.getMessage());
			}
		}
	}

	@RequestMapping("/save")
	public Response saveToFile(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			JSONObject json = parseJsonValue(jsonValue);
			String lpath = json.getString("lpath");
			String content = json.getString("content");
			if (CommonUtils.isNotBlank(content)) {
				content = content.replaceAll("≡", "+");
				content = content.replaceAll("∮", "%");
			}
			File file = new File(lpath);
			if (file.exists()) {
				try {
					FileUtils.writeFileAsCover(lpath, content);
					result.success();
				} catch (IOException e) {
					result.fail("Error: " + e.getMessage());
				}
			} else {
				result.fail("文件不存在");
			}
		} else {
			result.fail("未登录或登录超时");
		}
		return result;
	}

	@RequestMapping("/sync")
	public Response syncToServer(@RequestParam("jsonValue") String jsonValue) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user) {
			try {
				JSONObject json = parseJsonValue(jsonValue);
				String lpath = json.getString("lpath");
				String rpath = json.getString("rpath");
				String serverIp = json.getString("serverIp");
				String content = json.getString("content");
				boolean save = json.getBooleanValue("save");
				if (CommonUtils.isNotBlank(serverIp) && CommonUtils.isNotBlank(lpath) && CommonUtils.isNotBlank(rpath)) {
					File file = new File(lpath);
					if (file.exists()) {
						// 先保存到本地
						if (save) {
							if (CommonUtils.isNotBlank(content)) {
								content = content.replaceAll("≡", "+");
								content = content.replaceAll("∮", "%");
							}
							FileUtils.writeFileAsCover(lpath, content);
						}
						ShellInstance shell = ShellManager.getByIp(user.getUserId(), serverIp);
						SftpTransfileMonitor monitor = new SftpTransfileMonitor();
						shell.getLinuxOs().getSftp().put(lpath, rpath, monitor);
						while (!monitor.isFinished()) {
							Thread.sleep(10);
						}
						result.success();
					} else {
						result.fail("文件不存在");
					}
				} else {
					result.fail("参数不正确");
				}
			} catch (Exception e) {
				result.fail("Error: " + e.getMessage());
			}
		} else {
			result.fail("未登录或登录超时");
		}
		return result;
	}

	@RequestMapping("/put")
	public Response uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("serverIp") String serverIp, @RequestParam("rpath") String rpath,
			@RequestParam("fileCode") String fileCode) {
		Response result = new Response();
		YxosUser user = getYxosUser();
		if (null != user && CommonUtils.isNotBlank(serverIp) && CommonUtils.isNotBlank(rpath)) {
			final ShellInstance shell = ShellManager.getByIp(user.getUserId(), serverIp);
			FileOutputStream fos = null;
			try {
				String orgFileName = uploadFile.getOriginalFilename();
				String fileName = orgFileName;
				String fileSuffix = FileUtils.getFilePointSuffix(orgFileName);
				fileName = CommonUtils.getUUID() + fileSuffix;
				byte[] b = new byte[1024];
				String fileDir = Utils.getTempDir(user.getUserId());
				fos = new FileOutputStream(new File(fileDir + fileName));

				InputStream ins = uploadFile.getInputStream();
				logger.debug("上传文件：" + orgFileName);

				int i = ins.read(b);
				while (i != -1) {
					fos.write(b, 0, i);
					i = ins.read(b);
				}
				try {
					if (null != fos) {
						fos.flush();
						fos.close();
					}
					JSONObject data = new JSONObject();
					data.put("lpath", fileDir + fileName);
					data.put("rpath", rpath + "/" + orgFileName);
					result.success().setDataToRtn(data);
					final String ip = serverIp;
					final String l = fileDir + fileName;
					final String r = rpath + "/" + orgFileName;
					final String s = user.getUserId() + "/" + fileName;
					new Thread() {
						public void run() {
							SftpTransfileMonitor monitor = new SftpTransfileMonitor() {
								DataPackage dataPackage = new DataPackage();
								long max = 0;

								public void init(int op, String src, String dest, long max) {
									this.max = max;
									dataPackage.setReq("linux.upload");
									dataPackage.setFlag(ip);
									dataPackage.setData("{\"type\":\"start\"}");
									send();
								}

								public boolean count(long count) {
									dataPackage.setData("{\"type\":\"trans\",\"value\":" + ((float) count / (float) max) + "}");
									send();
									return true;
								}

								private void send() {
									try {
										shell.getClient().send(dataPackage);
									} catch (Exception e) {
									}
								}
							};
							DataPackage dataPackage = new DataPackage();
							dataPackage.setReq("linux.upload");
							dataPackage.setFlag(ip);
							try {
								shell.getLinuxOs().getSftp().put(l, r, monitor);
								while (!monitor.isFinished()) {
									Thread.sleep(10);
								}
								dataPackage.setData("{\"type\":\"upload\",\"value\":\"" + s + "\"}");
								shell.getClient().send(dataPackage);
							} catch (Exception e) {
								dataPackage.setData("{\"type\":\"error\",\"msg\":\"" + e.getMessage() + "\"}");
								try {
									shell.getClient().send(dataPackage);
								} catch (Exception e1) {
								}
							}
						}
					}.start();
				} catch (IOException e) {
				}
			} catch (Exception e) {
				logger.error("文件上传失败：" + e.getMessage(), e);
			}
		}
		return result;
	}

}
