# Java 25 Environment Setup for PowerShell
# Add this to your PowerShell profile or run manually before building

function Set-Java25 {
    $javaHome = "C:\Users\Pete\java\corretto25"
    $env:JAVA_HOME = $javaHome
    $env:PATH = "$javaHome\bin;" + $env:PATH

    Write-Host "✓ Java 25 Environment Configured"
    Write-Host "  JAVA_HOME: $javaHome"
    Write-Host "  Version: $(java -version 2>&1 | Select-Object -First 1)"
}

Set-Java25
