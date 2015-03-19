/*
 * Copyright (c) 2006-2007 Hyperic, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;

import org.hyperic.sigar.cmd.SigarCommandBase;

/**
 * Display all process information.
 */
public class SystemInfo extends SigarCommandBase {

    private boolean isSingleProcess;

    public SystemInfo() {
        super();
    }

    protected boolean validateArgs(String[] args) {
        return true;
    }

    public String getUsageShort() {
        return "Display all process info";
    }

    public boolean isPidCompleter() {
        return true;
    }

    public SystemSlice getSlice() {

        /*

        long[] pids = this.shell.findPids(args);

        for (int i=0; i<pids.length; i++) {
           try {
                output(String.valueOf(pids[i]));
            } catch (SigarPermissionDeniedException e) {
                 println(this.shell.getUserDeniedMessage(pids[i]));
            } catch (SigarException e) {
                println("(" + e.getMessage() + ")");
            }
             println("\n------------------------\n");
         }

        */

        return new SystemSlice(1, 2, (long)3);
    }

    public void output(String[] args) throws SigarException {
    }

    //this is an example method that I am working off of
    /*
    public void output(String[] args) throws SigarException {
        this.isSingleProcess = false;

        if ((args.length != 0) && args[0].startsWith("-s")) {
            this.isSingleProcess = true;
        }

        if (this.isSingleProcess) {
            for (int i=1; i<args.length; i++) {
                try {
                    output(args[i]);
                } catch (SigarException e) {
                    println("(" + e.getMessage() + ")");
                }
                println("\n------------------------\n");
            }
        }
        else {
            long[] pids = this.shell.findPids(args);

            for (int i=0; i<pids.length; i++) {
                try {
                    output(String.valueOf(pids[i]));
                } catch (SigarPermissionDeniedException e) {
                    println(this.shell.getUserDeniedMessage(pids[i]));
                } catch (SigarException e) {
                    println("(" + e.getMessage() + ")");
                }
                println("\n------------------------\n");
            }
        }
    }
    */

}
