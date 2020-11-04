### Install
`npm install gt-push-sdk`

### Init a push Client
First you need create an app at [https://dev.getui.com](https://dev.getui.com), and then get your `APPKEY` and `MASTERSECRET` in the following page:
![](http://i.imgur.com/uXClB4P.png)

Init a push client:

    var GeTui = require( 'gt-push-sdk' );
    var api = new GeTui('HOST', 'APPKEY', 'MASTERSECRET');
    // The HOST can be http://sdk.open.api.igexin.com/apiex.htm or https://api.getui.com/apiex.htm.



### More Docs and Demos
Please visit [http://docs.getui.com/server/nodejs/start/](http://docs.getui.com/server/nodejs/start/)

