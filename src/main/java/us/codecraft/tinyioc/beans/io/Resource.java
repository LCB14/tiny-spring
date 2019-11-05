package us.codecraft.tinyioc.beans.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource是spring内部定位资源的接口。
 * @author yihua.huang@dianping.com
 *
 * 接口，标识一个外部资源。通过 getInputStream() 方法 获取资源的输入流 。
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}
