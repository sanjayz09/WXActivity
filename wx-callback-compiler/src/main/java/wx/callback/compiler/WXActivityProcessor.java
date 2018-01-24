package wx.callback.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import wx.callback.WXActivity;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by sanjay.zsj09@gmail.com on 2017/10/26.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class WXActivityProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(WXActivity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(WXActivity.class);
        for (Element element : set) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "only support class");
            }

            if (!isActivity(element.asType())) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "is not a Activity", element);
            }

            String clazzName = element.getSimpleName().toString();
            String packageName = processingEnv.getElementUtils()
                    .getPackageOf(element).asType().toString();

            TypeSpec classTypeSpec = TypeSpec.classBuilder(clazzName)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.get(packageName, clazzName))
                    .build();

            String appId = element.getAnnotation(WXActivity.class).value();

            String targetPackageName = appId + ".wxapi";

            if (packageName.equals(targetPackageName)) {
                continue;
            }

            JavaFile javaFile = JavaFile.builder(targetPackageName, classTypeSpec)
                    .addFileComment(" This codes are generated automatically. Do not modify!")
                    .build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isActivity(TypeMirror typeMirror) {
        List<? extends TypeMirror> supers = processingEnv.getTypeUtils().
                directSupertypes(typeMirror);

        if (supers.size() == 0) {
            return false;
        }

        for (TypeMirror superType : supers) {
            if (superType.toString().equals("android.app.Activity") ||
                    isActivity(superType)) {
                return true;
            }
        }
        return false;
    }
}
