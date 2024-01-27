package fr.ensimag.deca;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: File not found");
        }
        if (options.getPrintBanner()) {
            if(!options.getOtherThanBOption()){
                System.out.println("GL g22");
                System.exit(0);
            }
            else{
                System.err.println("Error: decac -b works only without arguments !");
            }
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.

            // Collecting the files and turning them to ready to be compiled.
            List<File> files = options.getSourceFiles();
            List<DecacCompiler> decacCompilerFiles = new ArrayList<DecacCompiler>();
            for(File file:files){
                decacCompilerFiles.add(new DecacCompiler(options, file));
            }

            List<Future<Boolean>> futures = new ArrayList<>();

            // Parallel execution of DecacCompiler.compile()
            ExecutorService executorService = Executors.newFixedThreadPool(files.size());

            for(DecacCompiler toBeCompiledFile : decacCompilerFiles){
                // We are creating the task to be assign for each thread later on
                Callable<Boolean> compilationTask = ()->{
                    return toBeCompiledFile.compile();
                };
                // Assigning the task to out thread pool
                futures.add(executorService.submit(compilationTask));
            }

            // Waiting for all tasks to complete
            for(Future<Boolean> future : futures){
                try{
                   if(future.get()) {
                       error = true;
                   }
                }
                catch( InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
            }



        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
