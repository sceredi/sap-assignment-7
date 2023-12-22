import application.UserService
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import domain.*
import domain.model.UserModel
import infrastructure.database.UserMongoRepository
import infrastructure.web.RestService
import infrastructure.web.handlers.UserHandler
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodec
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

class RestServiceLauncher {


    fun launch() {
        val port = 8080
        val connectionString = ConnectionString(System.getenv("MONGO_URI")?:"mongodb://users-db:27017")
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(
                CodecRegistries.fromRegistries(
                    CodecRegistries.fromCodecs(UuidCodec(UuidRepresentation.STANDARD)),
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
                )
            )
            .build()


        val client = MongoClient.create(settings)
        val db = client.getDatabase("users_db")
        val users = db.getCollection<MongoUser>("users")

        val userHandler = UserHandler(UserService(UserModel(UserMongoRepository(users))))

        RestService(port, userHandler).init()

    }
}