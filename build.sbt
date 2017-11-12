
enablePlugins(TutPlugin)


tutSourceDirectory := file("tut")
tutTargetDirectory := file(".")

scalacOptions += "-Ypartial-unification"

val monocleVersion = "1.5.0-cats-M2"


libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-RC1",
  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test"
)
