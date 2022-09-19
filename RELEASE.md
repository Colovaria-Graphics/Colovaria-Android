Release Process
===============

 1. Update the module `CHANGELOG.md` file with relevant info and date.
 2. Update version number in `gradle.properties` files...
 3. Commit: `git commit -am "Prepare version X.Y.Z-<Module>"`.
 4. Tag: `git tag -a X.Y.Z-<Module> -m "Version-X.Y.Z-<Module>"`.
 5. Release: `./publish.sh`.
 6. Push: `git push && git push --tags`.
 7. Create release page in GitHub.
 8. Create release in `https://s01.oss.sonatype.org`.
