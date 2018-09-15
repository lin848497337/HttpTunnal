import ajax from "../common/service.js"


export default{
  name:"userService",
  doLogin(u, p, callback){
    ajax.httpGet("v1/user/login", {
      account: u,
      password: p
    },callback);
  },
  doRegister(u, p, callback){
    ajax.httpPost("v1/user/register", {
      account: u,
      password: p
    },callback);
  }
}
