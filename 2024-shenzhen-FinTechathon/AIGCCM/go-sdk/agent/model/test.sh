

curl -X POST http://localhost:10100/collection/select \
-H "Content-Type: application/json" \
-d '{
    "collection_id": "51a70b9213265e67",
    "collection_name": "Sample Artwork",
    "collection_matrix": [
        [
            1,
            2,
            3
        ],
        [
            4,
            5,
            6
        ],
        [
            7,
            8,
            9
        ]
    ],
    "collection_make": "Artwork Maker",
    "collection_record": "Artwork Record",
    "owner_id": "owner_456",
    "certificate_time": "2024-12-01T13:21:49Z",
    "certificate_organization": "Artwork Certification Org",
    "collection_semantic": "12345"
}'

curl -X POST http://1.95.32.15:10100/collection/insert \
-H "Content-Type: application/json" \
-d '{
    "collection_id": "unique_id_123",
    "collection_name": "Sample Artwork",
    "collection_matrix": [
        [
            1,
            2,
            3
        ],
        [
            4,
            5,
            6
        ],
        [
            7,
            8,
            9
        ]
    ],
    "collection_make": "Artwork Maker",
    "collection_record": "Artwork Record",
    "owner_id": "owner_456",
    "certificate_time": "2024-12-01T13:21:49Z",
    "certificate_organization": "Artwork Certification Org",
    "collection_semantic": "12345"
}'

curl -X POST http://localhost:10100/collection/delete \
-H "Content-Type: application/json" \
-d '{
    "collection_id": "unique_id_123",
    "collection_name": "Sample Artwork",
    "collection_matrix": [
        [
            1,
            2,
            3
        ],
        [
            4,
            5,
            6
        ],
        [
            7,
            8,
            9
        ]
    ],
    "collection_make": "Artwork Maker",
    "collection_record": "Artwork Record",
    "owner_id": "owner_456",
    "certificate_time": "2024-12-01T13:21:49Z",
    "certificate_organization": "Artwork Certification Org",
    "collection_semantic": "12345"
}'

curl -X POST http://localhost:10100/collection/update \
-H "Content-Type: application/json" \
-d '{
    "collection_id": "unique_id_123",
    "collection_name": "Sample Artwork",
    "collection_matrix": [
        [
            1,
            2,
            3
        ],
        [
            4,
            5,
            6
        ],
        [
            7,
            8,
            9
        ]
    ],
    "collection_make": "Artwork Maker",
    "collection_record": "Artwork Record",
    "old_owner_id": "owner_456",
    "owner_id": "owner_789",
    "certificate_time": "2024-12-01T13:21:49Z",
    "certificate_organization": "Artwork Certification Org",
    "collection_semantic": "12345"
}'


curl -X POST http://localhost:10100/collection/transfer \
-H "Content-Type: application/json" \
-d '{
    "user_id1": "owner_456",
    "user_id2": "owner_789",
    "collection_id": "unique_id_123",
    "transfer_money": 1
}'

curl -X POST http://localhost:10100/account/insert \
-H "Content-Type: application/json" \
-d '{
    "user_id": "owner_456",
    "user_money": "100",
    "user_name": "Alice",
    "user_icon": "icon_456"}'

curl -X POST http://localhost:10100/account/insert \
-H "Content-Type: application/json" \
-d '{
    "user_id": "owner_789",
    "user_money": "100",
    "user_name": "Bob",
    "user_icon": "icon_456"
    }'