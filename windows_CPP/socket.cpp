#include<string>
#include<cstring>
#include<iostream>
#include<winsock.h>
#pragma comment(lib,"ws2_32.lib")
using namespace std;
void initialization();
void toClipboard(const char *);
void printClipboard();
	int send_len = 0;
	int recv_len = 0;
	int len = 0;
	//定义发送缓冲区和接受缓冲区
//	char send_buf[100];
    string send_buf;
	char recv_buf[1024];
	//定义服务端套接字，接受请求套接字
	SOCKET s_server;
	SOCKET s_accept;
	//服务端地址客户端地址
	SOCKADDR_IN server_addr;
	SOCKADDR_IN accept_addr;
	//填充服务端信息

    void getCommand();
    void send_clipboard();
    void receive_clipboard();
int main() {
	//定义长度变量
	//创建套接字
    initialization(); 
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.S_un.S_addr = htonl(INADDR_ANY);
	server_addr.sin_port = htons(8989);

	s_server = socket(AF_INET, SOCK_STREAM, 0);
	if (bind(s_server, (SOCKADDR *)&server_addr, sizeof(SOCKADDR)) == SOCKET_ERROR) {
		cout << "套接字绑定失败！" << endl;
		WSACleanup();
	}
	else {
		cout << "套接字绑定成功！" << endl;
	}
	//设置套接字为监听状态
	if (listen(s_server, SOMAXCONN) < 0) {
		cout << "设置监听状态失败！" << endl;
		WSACleanup();
	}
	else {
		cout << "设置监听状态成功！" << endl;
	}
	cout << "服务端正在监听连接，请稍候...." << endl;
	//接受连接请求
	len = sizeof(SOCKADDR);
	s_accept = accept(s_server, (SOCKADDR *)&accept_addr, &len);
	if (s_accept == SOCKET_ERROR) {
		cout << "连接失败！" << endl;
		WSACleanup();
		return 0;
	}
	cout << "连接建立，准备接受数据" << endl;
    cout<<"ip:"<<inet_ntoa(accept_addr.sin_addr)<<"  port="<<ntohs(accept_addr.sin_port)<<endl;
	//接收数据
    getCommand();
	//关闭套接字
	closesocket(s_server);
	closesocket(s_accept);
	//释放DLL资源
	WSACleanup();
	return 0;
}
void getCommand()
{
    string command;
    cin>>command;
    while(1)
    {
        if(strcmp(command.c_str(),"copy")==0)
        {
          cout<<"输入copy内容 :"<<endl;
            send_clipboard();
        }else if(strcmp(command.c_str(),"paste")==0)
        {
            receive_clipboard();
        }else if(strcmp(command.c_str(),"quit")==0)
        {
            return ;
        }
        else{
            cout<<"command not defined, try it again"<<endl;
        }
        cin>>command;
    };
}
void send_clipboard()
{
        getline(cin,send_buf);
        send_buf.append(1,'\n');
		send_len = send(s_accept, send_buf.c_str(),send_buf.size(), 0);
        cout<<send_buf<<endl;
        cout<<"send_len :"<<send_len<<endl;
}
void receive_clipboard()
{
    
	int recv_message = recv(s_accept, recv_buf, 1024, 0);
    if(recv_message>0)
    {
        cout<<"message received "<<endl;
        cout<<recv_buf<<endl<<"lenth: "<<strlen(recv_buf)<<endl;
         
            toClipboard(recv_buf);
        memset(recv_buf,0,sizeof(char)*strlen(recv_buf));
    }else if(recv_message==0)
    {
        cout<<"connection closed"<<endl;
    }else 
        cout<<"recv failed : "<<WSAGetLastError()<<endl;

}

void initialization() {
	//初始化套接字库
	WORD w_req = MAKEWORD(2, 2);//版本号
	WSADATA wsadata;
	int err;
	err = WSAStartup(w_req, &wsadata);
	if (err != 0) {
		cout << "初始化套接字库失败！" << endl;
	}
	else {
		cout << "初始化套接字库成功！" << endl;
	}
	//检测版本号
	if (LOBYTE(wsadata.wVersion) != 2 || HIBYTE(wsadata.wHighVersion) != 2) {
		cout << "套接字库版本号不符！" << endl;
		WSACleanup();
    
	}
	else {
		cout << "套接字库版本正确！" << endl;
	}
	//填充服务端地址信息
 
}


void toClipboard(const char*s) {
    OpenClipboard(0);
    EmptyClipboard();
    HGLOBAL hg = GlobalAlloc(GMEM_MOVEABLE, strlen(s)+1);
    if (!hg) {
        CloseClipboard();
        cout<<"allocate memory  failed"<<endl;
        return;
    }
    memcpy(GlobalLock(hg), s, (strlen(s)+1)*sizeof(char));
    GlobalUnlock(hg);
    SetClipboardData(CF_TEXT, hg);
    CloseClipboard();
    GlobalFree(hg);
}
void printClipboard()
{
    HANDLE h;
    if(OpenClipboard(NULL))
    {
        h=GetClipboardData(CF_TEXT);
        printf("%s",(char*)h);
        CloseClipboard();
    }
}
