package com.example.chattingback.controller.webSocket;

public class WebSocketListener {
    /**
     * 监听器类:主要任务是用ServletRequest将我们的HttpSession携带过去
     */
    @Component
    public class RequestListener implements ServletRequestListener {
        @Override
        public void requestInitialized(ServletRequestEvent sre)  {

            //将所有request请求都携带上httpSession
            ((HttpServletRequest) sre.getServletRequest()).getSession();
        }
        public RequestListener() {}

        @Override
        public void requestDestroyed(ServletRequestEvent arg0)  {}
    }


}
