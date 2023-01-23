package etape2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class StubGenerator {
    public static void main(String[] args) {
        try {
            String className = args[0];
            String interfaceSuffix = args[1];

            Class<?> interf;

            String pack = ".";
            
            if (args.length == 3) {
                pack = args[2];

                interf = Class.forName(pack + '.' + className + interfaceSuffix);
            } else {
                interf = Class.forName(className + interfaceSuffix);
            }
            
            Method[] methods = interf.getDeclaredMethods();

            File file = new File("src/" + pack + '/' + className + "_stub.java");

            file.createNewFile();

            FileWriter fw = new FileWriter(file);

            if (!pack.equals(".")) {
                fw.write("package " + pack + ";\n\n");
            }

            fw.write("public class " + className + "_stub extends SharedObject implements " + className + interfaceSuffix + ", java.io.Serializable {\n");

            fw.write("\tpublic " + className + "_stub (int id, Object obj) {\n");
            fw.write("\t\tsuper(id, obj);\n\t}\n");

            for (Method method : methods) {
                fw.write("\n\tpublic " + method.getReturnType().getName() + ' ' + method.getName() + "(");

                Parameter[] params = method.getParameters();

                boolean first = true;

                for (Parameter param : params) {
                    if (!first)
                        fw.write(", ");
                    else
                        first = false;

                    fw.write(param.getType().getName() + ' ' + param.getName());
                }

                fw.write(") {\n\t\t" + className + " o = (" + className + ") obj;\n\t\t");

                if (!method.getReturnType().getName().equals("void")) {
                    fw.write("return ");
                } 
                fw.write("o." + method.getName() + "(");

                first = true;
                for (Parameter param : params) {
                    if (!first)
                        fw.write(", ");
                    else
                        first = false;

                    fw.write(param.getName());
                }

                fw.write(");\n\t}\n");
            }
            fw.write("}");

            fw.close();

        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
