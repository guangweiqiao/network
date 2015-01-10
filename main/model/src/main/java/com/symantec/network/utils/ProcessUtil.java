package com.symantec.network.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessUtil {

  /**
   * launch a process via given string
   * @param cmd command with parameter if it has
   * Launch a process and get it's output
   * @return output, null if failed
   * @throws InterruptedException 
   * @throws IOException 
   * @throws RuntimeException 
   * @throws TimeoutException 
   */
  public static String launchProcess(String... cmd) throws TimeoutException, RuntimeException, IOException, InterruptedException {
    return launchProcess(0, false, cmd);
  }
  
  /**
   * launch a process via given cmd, verify exit code and return its output
   * @param cmd
   * @return output string, or null if failed
   * @throws RuntimeException throw if exit code not equal 0
   * @throws InterruptedException 
   * @throws IOException 
   * @throws TimeoutException 
   */
  public static String launchProcessAndVerifySuccess(String... cmd) throws RuntimeException, TimeoutException, IOException, InterruptedException {
    return launchProcess(0, true, cmd);
  }
  
  /**
   * launch a process via given cmd and return its output
   * @param timeout the maximum time to wait, time unit is million seconds. 0 for waiting until the process is terminated
   * @param verifyExitCode verify exit code 
   * @param cmd 
   * @return process' output string, null if failed
   * @throws TimeoutException throw if process execution time exceeded the @timeout
   * @throws RuntimeException throw if process returned exit code does not equal to expected, exception error message is process output message 
   * @throws IOException 
   * @throws InterruptedException 
   */
  public static String launchProcess(long timeout, boolean verifyExitCode, String... cmd) throws TimeoutException, RuntimeException, IOException, InterruptedException {
    StringBuilder output = new StringBuilder();
    
    ProcessBuilder pb = new ProcessBuilder(cmd);
    Process process = pb.start();
    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    if (0 == timeout)
    {
      process.waitFor();
    }
    else
    {
      if (!process.waitFor(timeout, TimeUnit.MILLISECONDS))
      {
        process.destroy();
        throw new TimeoutException();
      }
    }
    
    String line = null;
    while ((line = br.readLine()) != null)
    {
      output.append(line + System.lineSeparator());
    }

    if (verifyExitCode && process.exitValue() != 0)
    {
      throw new RuntimeException(output.toString());
    }

    br.close();
    
    return output.toString();
  }
}
