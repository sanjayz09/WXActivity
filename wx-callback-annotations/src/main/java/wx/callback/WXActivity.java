package wx.callback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sanjay.zsj09@gmail.com on 2017/10/26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface WXActivity {
    String value();
}
